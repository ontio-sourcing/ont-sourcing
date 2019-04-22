package com.ontology.sourcing.service.utils;

import com.alibaba.fastjson.JSON;
import com.github.ontio.OntSdk;
import com.ontology.sourcing.dao.Event;
import com.ontology.sourcing.dao.ddo.ActionOntid;
import com.ontology.sourcing.dao.sfl.SFLNotary;
import com.ontology.sourcing.mapper.EventMapper;
import com.ontology.sourcing.mapper.ddo.ActionOntidMapper;
import com.ontology.sourcing.mapper.sfl.SFLNotaryMapper;
import com.ontology.sourcing.model.ddo.DDOPojo;
import com.ontology.sourcing.service.OntidService;
import com.ontology.sourcing.utils.GlobalVariable;
import com.ontology.sourcing.utils.HttpUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;

@Service
public class SyncService {

    //
    private PropertiesService propertiesService;
    private OntSdk ontSdk;

    //
    private EventMapper eventMapper;
    private ActionOntidMapper actionOntidMapper;
    private OntidService ontidService;
    //
    SFLNotaryMapper sflNotaryMapper;

    @Autowired
    public SyncService(EventMapper eventMapper,
                       ActionOntidMapper actionOntidMapper,
                       OntidService ontidService,
                       PropertiesService propertiesService,
                       SFLNotaryMapper sflNotaryMapper) {
        this.eventMapper = eventMapper;
        this.actionOntidMapper = actionOntidMapper;
        this.ontidService = ontidService;
        //
        this.propertiesService = propertiesService;
        ontSdk = GlobalVariable.getOntSdk(propertiesService.ontologyUrl, propertiesService.walletPath);
        //
        this.sflNotaryMapper = sflNotaryMapper;
    }

    public void confirmTxAndDDO(String ontid, String txhash) {
        Executors.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6 * 1000);
                    Object event = ontSdk.getConnect().getSmartCodeEvent(txhash);
                    while (event == null || StringUtils.isEmpty(event)) {
                        // sdk.addAttributes(ontId,password,key,valueType,value);
                        Thread.sleep(6 * 1000);
                        event = ontSdk.getConnect().getSmartCodeEvent(txhash);
                    }
                    // 获取交易所在的区块高度
                    int height = ontSdk.getConnect().getBlockHeightByTxHash(txhash);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void confirmTx(String txhash) {
        Executors.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6 * 1000);
                    Object event = ontSdk.getConnect().getSmartCodeEvent(txhash);
                    while (event == null || StringUtils.isEmpty(event)) {
                        // sdk.addAttributes(ontId,password,key,valueType,value);
                        Thread.sleep(6 * 1000);
                        event = ontSdk.getConnect().getSmartCodeEvent(txhash);
                    }
                    // 获取交易所在的区块高度
                    int height = ontSdk.getConnect().getBlockHeightByTxHash(txhash);
                    //
                    String eventStr = JSON.toJSONString(event);
                    Event record = new Event();
                    record.setTxhash(txhash);
                    record.setEvent(eventStr);
                    record.setHeight(height);
                    record.setCreateTime(new Date());
                    eventMapper.save(record);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void confirmTxSFL(String txhash, String filehash) {
        Executors.newCachedThreadPool().submit(new Runnable() {
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
                    e.printStackTrace();
                }
            }
        });

    }
}
