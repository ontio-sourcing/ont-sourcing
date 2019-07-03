package com.ontology.sourcing.service.util;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.github.ontio.network.exception.ConnectorException;
import com.ontology.sourcing.model.dao.Event;
import com.ontology.sourcing.model.dao.ddo.ActionOntid;
import com.ontology.sourcing.model.dao.sfl.SFLNotary;
import com.ontology.sourcing.mapper.EventMapper;
import com.ontology.sourcing.mapper.ddo.ActionOntidMapper;
import com.ontology.sourcing.mapper.sfl.SFLNotaryMapper;
import com.ontology.sourcing.model.dto.ddo.DDOPojo;
import com.ontology.sourcing.service.OntidService;
import com.ontology.sourcing.util.HttpUtil;
import com.ontology.sourcing.util.ThreadUtil;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

@Service
public class SyncService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(SyncService.class);

    //
    private EventMapper eventMapper;
    private ActionOntidMapper actionOntidMapper;
    //
    private SFLNotaryMapper sflNotaryMapper;
    //
    private OntidService ontidService;
    private ChainService chainService;

    @Autowired
    public SyncService(EventMapper eventMapper, ActionOntidMapper actionOntidMapper, SFLNotaryMapper sflNotaryMapper, OntidService ontidService, ChainService chainService) {
        this.eventMapper = eventMapper;
        this.actionOntidMapper = actionOntidMapper;
        //
        this.sflNotaryMapper = sflNotaryMapper;
        //
        this.ontidService = ontidService;
        this.chainService = chainService;
    }

    public void confirmTxAndDDO(String ontid, String txhash) {

        ThreadUtil.getInstance().submit(new Runnable() {

            @Override
            public void run() {

                for (int retry = 0; retry < 5; retry++) {

                    try {
                        Thread.sleep(6 * 1000);
                        Object event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        while (event == null || StringUtils.isEmpty(event)) {
                            // sdk.addAttributes(ontId,password,key,valueType,value);
                            Thread.sleep(6 * 1000);
                            event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        }
                        // 获取交易所在的区块高度
                        int height = chainService.ontSdk.getConnect().getBlockHeightByTxHash(txhash);
                        //
                        String eventStr = JSON.toJSONString(event);
                        Event record = new Event();
                        record.setTxhash(txhash);
                        record.setEvent(eventStr);
                        record.setHeight(height);
                        record.setCreateTime(new Date());
                        eventMapper.save(record);
                        //
                        DDOPojo ddoPojo = ontidService.getDDO(ontid);
                        // 更新本地的DDO
                        ActionOntid actionOntidRecord = actionOntidMapper.findByOntid(ontid);
                        actionOntidRecord.setDdo(JSON.toJSONString(ddoPojo));
                        actionOntidRecord.setUpdateTime(new Date());
                        actionOntidMapper.save(actionOntidRecord);
                        //
                        break;
                    } catch (ConnectorException | IOException e) {
                        //
                        logger.error(e.getMessage());
                        //
                        chainService.switchOntSdk();
                        //
                        continue;
                    } catch (Exception e) {
                        //
                        logger.error(e.getMessage());
                        //
                        break;
                    }
                }
            }
        });

    }

    public void confirmTx(String txhash) {

        ThreadUtil.getInstance().submit(new Runnable() {

            @Override
            public void run() {

                for (int retry = 0; retry < 5; retry++) {
                    try {
                        Thread.sleep(6 * 1000);
                        Object event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        while (event == null || StringUtils.isEmpty(event)) {
                            // sdk.addAttributes(ontId,password,key,valueType,value);
                            Thread.sleep(6 * 1000);
                            event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        }
                        // 获取交易所在的区块高度
                        int height = chainService.ontSdk.getConnect().getBlockHeightByTxHash(txhash);
                        //
                        String eventStr = JSON.toJSONString(event);
                        Event record = new Event();
                        record.setTxhash(txhash);
                        record.setEvent(eventStr);
                        record.setHeight(height);
                        record.setCreateTime(new Date());
                        eventMapper.save(record);
                        //
                        break;
                    } catch (ConnectorException | IOException e) {
                        //
                        // logger.error(e.getMessage());
                        logger.error(e.getMessage());

                        //
                        chainService.switchOntSdk();
                        //
                        continue;
                    } catch (Exception e) {
                        //
                        logger.error(e.getMessage());
                        //
                        break;
                    }
                }
            }
        });

    }

    public void confirmTxSFL(String txhash, String filehash) {

        ThreadUtil.getInstance().submit(new Runnable() {

            @Override
            public void run() {

                //
                JSONObject obj = new JSONObject();
                obj.put("content", filehash);
                obj.put("hash", txhash);
                //
                int retry = 0;
                //
                try {
                    while (retry <= 10) {
                        //
                        Thread.sleep(10 * 1000L);
                        //
                        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/indentify", HttpUtil.FORM, obj);
                        //
                        JSONObject response = new JSONObject(httpInfo.responseBody);
                        //
                        String data = (String) response.get("data");
                        System.out.println(data);
                        if (data.contains("数据未被篡改")) { // TODO
                            //
                            SFLNotary notary = sflNotaryMapper.findByTxhash(txhash);
                            notary.setConfirm(1);
                            notary.setUpdateTime(new Date());
                            sflNotaryMapper.save(notary);
                            //
                            break;
                        }
                        //
                        retry++;
                    }
                } catch (InterruptedException | IOException e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }
}
