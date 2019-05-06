package com.ontology.sourcing.service.util;

import ch.qos.logback.classic.Logger;
import com.ontology.sourcing.dao.ddo.ActionIndex;
import com.ontology.sourcing.dao.contract.ContractIndex;
import com.ontology.sourcing.mapper.contract.ContractIndexMapper;
import com.ontology.sourcing.mapper.ddo.ActionIndexMapper;
import com.ontology.sourcing.util.GlobalVariable;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TableService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(TableService.class);

    //
    private ActionIndexMapper actionIndexMapper;
    private ContractIndexMapper contractIndexMapper;

    //
    private PropertiesService propertiesService;


    @Autowired
    public TableService(ActionIndexMapper actionIndexMapper, ContractIndexMapper contractIndexMapper, PropertiesService propertiesService) {
        this.actionIndexMapper = actionIndexMapper;
        this.contractIndexMapper = contractIndexMapper;
        this.propertiesService = propertiesService;
    }

    //
    private static void setThreadName() {
        Thread thr = Thread.currentThread();
        thr.setName("TableDetector" + "_" + thr.getId());
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("TableDetector PostConstruct start ...");

        // 程序启动时，检测当前使用的action表
        ActionIndex actionIndex = actionIndexMapper.selectCurrent();
        if (actionIndex != null) {
            GlobalVariable.CURRENT_ACTION_TABLE_INDEX = actionIndex.getId();
            GlobalVariable.CURRENT_ACTION_TABLE_NAME = actionIndex.getName();
        }

        // 程序启动时，检测当前使用的contract表
        ContractIndex contractIndex = contractIndexMapper.selectCurrent();
        if (contractIndex != null) {
            GlobalVariable.CURRENT_CONTRACT_TABLE_INDEX = contractIndex.getId();
            GlobalVariable.CURRENT_CONTRACT_TABLE_NAME = contractIndex.getName();
        }

        //
        new Thread(this::detectActionTable).start();
        // 稍微错开一点
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //
        new Thread(this::detectContractTable).start();
    }

    //
    private void detectActionTable() {

        //
        setThreadName();

        //
        while (true) {

            //
            int count = actionIndexMapper.count();
            if (count == 0) {
                // 第一次创建表
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String newTableName = "tbl_action_" + sdf.format(new Date(System.currentTimeMillis()));
                actionIndexMapper.createNewTable(newTableName);

                // 在表目录中加入新表，并设置flag为1
                ActionIndex newActionTable = new ActionIndex();
                newActionTable.setName(newTableName);
                newActionTable.setFlag(1);
                int newTableID = actionIndexMapper.insert(newActionTable);

                // 切换全局表名
                GlobalVariable.CURRENT_ACTION_TABLE_INDEX = newTableID;
                GlobalVariable.CURRENT_ACTION_TABLE_NAME = newTableName;
            }

            // 查询当前使用的表
            ActionIndex currentActionTable = actionIndexMapper.selectCurrent();
            String currentActionTableName = currentActionTable.getName();
            int size = actionIndexMapper.getTableSize(currentActionTableName);

            //
            int limit = propertiesService.TABLE_SIZE_LIMIT;
            if (size > limit) {

                // 创建新表
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String newTableName = "tbl_action_" + sdf.format(new Date(System.currentTimeMillis()));
                actionIndexMapper.createNewTable(newTableName);

                //
                logger.info("{} 旧表：{}，大小：{}G，超过limit：{}G，开始创建新表：{},", Thread.currentThread().getName(), currentActionTableName, size, limit, newTableName);

                // 在表目录中取消旧表，并设置flag为0
                currentActionTable.setFlag(0);
                actionIndexMapper.updateByPrimaryKeySelective(currentActionTable);

                // 在表目录中加入新表，并设置flag为1
                ActionIndex newActionTable = new ActionIndex();
                newActionTable.setName(newTableName);
                newActionTable.setFlag(1);
                int newTableID = actionIndexMapper.insert(newActionTable);

                // 切换全局表名
                GlobalVariable.CURRENT_ACTION_TABLE_INDEX = newTableID;
                GlobalVariable.CURRENT_ACTION_TABLE_NAME = newTableName;

            } else {

                // TODO 越接近limit，逼近的速度越快，再额外加上一个固定时间
                int t = (int) (propertiesService.TABLE_SIZE_DETECT_INTERVAL * ((float) (limit - size) / (float) limit)) + propertiesService.TABLE_SIZE_DETECT_INTERVAL * 1 / 24;
                logger.info("{} 开始休眠...{}ms", Thread.currentThread().getName(), t);

                //
                logger.info("{} 当前表：{}，大小：{}G", Thread.currentThread().getName(), currentActionTable.getName(), size);

                //
                try {
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("{} 停止休眠...", Thread.currentThread().getName());
            }

        }
    }

    //
    private void detectContractTable() {

        //
        setThreadName();

        //
        while (true) {

            //
            int count = contractIndexMapper.count();
            if (count == 0) {
                // 第一次创建表
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String newTableName = "tbl_contract_" + sdf.format(new Date(System.currentTimeMillis()));
                contractIndexMapper.createNewTable(newTableName);

                // 在表目录中加入新表，并设置flag为1
                ContractIndex newContractTable = new ContractIndex();
                newContractTable.setName(newTableName);
                newContractTable.setFlag(1);
                int newTableID = contractIndexMapper.insert(newContractTable);

                // 切换全局表名
                GlobalVariable.CURRENT_CONTRACT_TABLE_INDEX = newTableID;
                GlobalVariable.CURRENT_CONTRACT_TABLE_NAME = newTableName;
            }

            // 查询当前使用的表
            ContractIndex currentContractTable = contractIndexMapper.selectCurrent();
            String currentContractTableName = currentContractTable.getName();
            int size = contractIndexMapper.getTableSize(currentContractTableName);

            //
            int limit = propertiesService.TABLE_SIZE_LIMIT;
            if (size > limit) {

                // 创建新表
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String newTableName = "tbl_contract_" + sdf.format(new Date(System.currentTimeMillis()));
                contractIndexMapper.createNewTable(newTableName);

                //
                logger.info("{} 旧表：{}，大小：{}G，超过limit：{}G，开始创建新表：{},", Thread.currentThread().getName(), currentContractTableName, size, limit, newTableName);

                // 在表目录中取消旧表，并设置flag为0
                currentContractTable.setFlag(0);
                contractIndexMapper.updateByPrimaryKeySelective(currentContractTable);

                // 在表目录中加入新表，并设置flag为1
                ContractIndex newContractTable = new ContractIndex();
                newContractTable.setName(newTableName);
                newContractTable.setFlag(1);
                int newTableID = contractIndexMapper.insert(newContractTable);

                // 切换全局表名
                GlobalVariable.CURRENT_CONTRACT_TABLE_INDEX = newTableID;
                GlobalVariable.CURRENT_CONTRACT_TABLE_NAME = newTableName;

            } else {

                // TODO 越接近limit，逼近的速度越快，再额外加上一个固定时间
                int t = (int) (propertiesService.TABLE_SIZE_DETECT_INTERVAL * ((float) (limit - size) / (float) limit)) + propertiesService.TABLE_SIZE_DETECT_INTERVAL * 1 / 24;
                logger.info("{} 开始休眠...{}ms", Thread.currentThread().getName(), t);

                //
                logger.info("{} 当前表：{}，大小：{}G", Thread.currentThread().getName(), currentContractTable.getName(), size);

                //
                try {
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("{} 停止休眠...", Thread.currentThread().getName());
            }

        }
    }
}