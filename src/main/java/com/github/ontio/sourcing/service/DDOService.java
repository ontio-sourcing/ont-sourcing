package com.github.ontio.sourcing.service;

import com.alibaba.fastjson.JSON;
import com.github.ontio.common.Helper;
import com.github.ontio.core.ontid.Attribute;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.network.exception.RestfulException;
import com.github.ontio.sdk.info.IdentityInfo;
import com.github.ontio.sdk.wallet.Identity;
import com.github.ontio.sourcing.exception.exp.ExistedException;
import com.github.ontio.sourcing.mapper.ddo.ActionMapper;
import com.github.ontio.sourcing.model.dao.ddo.Action;
import com.github.ontio.sourcing.model.dto.ddo.DDOPojo;
import com.github.ontio.sourcing.model.dto.ddo.Owner;
import com.github.ontio.sourcing.model.dto.ddo.identity.OntidPojo;
import com.github.ontio.sourcing.service.util.ChainService;
import com.github.ontio.sourcing.service.util.PropertiesService;
import com.github.ontio.sourcing.util.GlobalVariable;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lambdaworks.crypto.SCryptUtil;
import com.github.ontio.sourcing.mapper.ddo.ActionOntidMapper;
import com.github.ontio.sourcing.model.dao.ddo.ActionOntid;
import com.github.ontio.sourcing.service.oauth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class DDOService {

    private Gson gson = new Gson();

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount;

    //
    private PropertiesService propertiesService;
    private ChainService chainService;
    private JWTService jwtService;
    //
    private ActionOntidMapper actionOntidMapper;
    private ActionMapper actionMapper;

    //
    @Autowired
    public DDOService(ActionOntidMapper actionOntidMapper,
                      ActionMapper actionMapper,
                      PropertiesService propertiesService,
                      ChainService chainService,
                      JWTService jwtService) {
        //
        this.actionOntidMapper = actionOntidMapper;
        this.actionMapper = actionMapper;
        //
        this.propertiesService = propertiesService;
        this.chainService = chainService;
        this.jwtService = jwtService;

        //
        payerAccount = GlobalVariable.getInstanceOfAccount(propertiesService.payerPrivateKey);
    }

    //
    public Map<String, String> createOntid(String username,
                                           String password) throws Exception {

        // 先检查username是否已注册
        Example example = new Example(ActionOntid.class);
        example.createCriteria().andCondition("username=", username);
        ActionOntid existed = actionOntidMapper.selectOneByExample(example);
        //
        if (existed != null) {
            throw new Exception("username existed.");
        }

        //
        Map<String, String> map = new HashMap<String, String>();

        // 创建数字身份 identity
        Identity identity = chainService.ontSdk.getWalletMgr().createIdentity(password);

        // 链上：注册 identity
        String rsp = chainService.ontSdk.nativevm().ontId().sendRegister(identity,
                                                                         password,
                                                                         payerAccount,
                                                                         chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                         GlobalVariable.DEFAULT_GAS_PRICE);  // 会将身份信息写入钱包文件
        map.put("txhash", rsp);

        //
        //        ontSdk.getWalletMgr().getWallet().clearIdentity(); // 清空身份信息
        //        ontSdk.getWalletMgr().writeWallet(); // 实际生效的操作

        //        Map keystore = WalletQR.exportIdentityQRCode(ontSdk.getWalletMgr().getWallet(), identity);
        //        map.put("keystore", JSON.toJSONString(keystore));

        String keystore = JSON.toJSONString(identity);
        map.put("keystore", keystore);
        String ontid = identity.ontid;
        map.put("ontid", ontid);

        // 写入本地表
        ActionOntid record = new ActionOntid();
        record.setUsername(username);
        record.setPassword(SCryptUtil.scrypt(password, GlobalVariable.scrypt_N, GlobalVariable.scrypt_r, GlobalVariable.scrypt_p));
        record.setOntid(ontid);
        record.setKeystore(keystore);
        record.setTxhash(rsp);
        record.setCreateTime(new Date());
        actionOntidMapper.insertSelective(record);

        //
        return map;
    }

    //
    public Map<String, String> login(String username,
                                     String password) throws Exception {

        // 先检查username是否已注册
        Example example = new Example(ActionOntid.class);
        example.createCriteria().andCondition("username=", username);
        ActionOntid existed = actionOntidMapper.selectOneByExample(example);
        //
        if (existed == null) {
            throw new Exception("username not exist.");
        }

        // String tmp = SCryptUtil.scrypt(password, GlobalVariable.scrypt_N, GlobalVariable.scrypt_r, GlobalVariable.scrypt_p);
        if (!SCryptUtil.check(password, existed.getPassword())) {
            throw new Exception("password error.");
        }

        //
        String user_ontid = existed.getOntid();

        Map<String, String> map = jwtService.getAccessToken(user_ontid);

        return map;
    }

    //
    public Map<String, String> updateOntidAttribute(String ontid,
                                                    String password,
                                                    String attributeJson) throws Exception {

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        OntidPojo ontidPojo = getPojoByOntid(ontid);

        //
        Attribute[] attributes = jsonToAttributes(attributeJson);

        //
        String rsp = chainService.ontSdk.nativevm().ontId().sendAddAttributes(ontid,
                                                                              password,
                                                                              ontidPojo.getControls().get(0).getSalt(),
                                                                              attributes,
                                                                              this.payerAccount,
                                                                              chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                              GlobalVariable.DEFAULT_GAS_PRICE);
        map.put("txhash", rsp);

        //
        return map;
    }

    //
    public Map<String, String> deleteEntityIdentityAttribute(String ontid,
                                                             String password,
                                                             String path_key) throws Exception {

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        OntidPojo ontidPojo = getPojoByOntid(ontid);

        //
        String rsp = chainService.ontSdk.nativevm().ontId().sendRemoveAttribute(ontid,
                                                                                password,
                                                                                ontidPojo.getControls().get(0).getSalt(),
                                                                                path_key,
                                                                                this.payerAccount,
                                                                                chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                                GlobalVariable.DEFAULT_GAS_PRICE);
        map.put("txhash", rsp);

        //
        return map;
    }

    //
    public Map<String, String> updateOntidControl(String ontid,
                                                  String ontidPassword,
                                                  String controlOntid) throws RestfulException, Exception {
        //
        Map<String, String> map = new HashMap<String, String>();

        //
        OntidPojo ontidPojo = getPojoByOntid(ontid);
        OntidPojo controlPojo = getPojoByOntid(controlOntid);

        // 验证是否已经在公钥列表中了
        String cpk = controlPojo.getControls().get(0).getPublicKey();
        DDOPojo ddoPojo = getDDO(ontidPojo.getOntid());
        for (Owner owner : ddoPojo.getOwners()) {
            if (cpk.equals(owner.getValue()))
                throw new ExistedException();
        }

        //
        String rsp = chainService.ontSdk.nativevm().ontId().sendAddPubKey(ontidPojo.getOntid(),
                                                                          ontidPassword,
                                                                          ontidPojo.getControls().get(0).getSalt(),
                                                                          controlPojo.getControls().get(0).getPublicKey(),
                                                                          payerAccount,
                                                                          chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                          GlobalVariable.DEFAULT_GAS_PRICE);
        map.put("txhash", rsp);

        //
        return map;
    }

    //
    public Map<String, String> updateOntidAttributeByControl(String ontid,
                                                             String attributeJson,
                                                             String controlOntid,
                                                             String controlPassword) throws Exception {

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        Attribute[] attributes = jsonToAttributes(attributeJson);

        //
        com.github.ontio.account.Account controlAccount = getAccountByOntidAndPassword(controlOntid, controlPassword);

        //
        String rsp = chainService.ontSdk.nativevm().ontId().sendAddAttributes(ontid,
                                                                              controlAccount,
                                                                              attributes,
                                                                              this.payerAccount,
                                                                              chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                              GlobalVariable.DEFAULT_GAS_PRICE);
        map.put("txhash", rsp);

        //
        return map;
    }

    //
    public Map<String, String> deleteEntityIdentityAttributeByControl(String ontid,
                                                                      String path_key,
                                                                      String controlOntid,
                                                                      String controlPassword) throws Exception {

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        com.github.ontio.account.Account controlAccount = getAccountByOntidAndPassword(controlOntid, controlPassword);

        //
        String rsp = chainService.ontSdk.nativevm().ontId().sendRemoveAttribute(ontid,
                                                                                controlAccount,
                                                                                path_key,
                                                                                this.payerAccount,
                                                                                chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                                GlobalVariable.DEFAULT_GAS_PRICE);
        map.put("txhash", rsp);

        //
        return map;
    }

    //
    public DDOPojo getDDO(String ontid) throws Exception {
        //
        String ddoStr = chainService.ontSdk.nativevm().ontId().sendGetDDO(ontid);
        DDOPojo ddoPojo = gson.fromJson(ddoStr, DDOPojo.class);
        //
        return ddoPojo;
    }

    //
    public Integer count(String ontid) {
        //
        Example example = new Example(Action.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("ontid=", ontid);
        //
        example.and(c1);
        //
        return actionMapper.selectCountByExample(example);
    }

    //
    public List<Action> getActionHistory(String ontid,
                                         int pageNum,
                                         int pageSize) {
        //
        PageHelper.startPage(pageNum, pageSize);
        //
        Example example = new Example(Action.class);
        example.createCriteria().andCondition("ontid=", ontid);
        example.setOrderByClause("id desc");
        List<Action> list = actionMapper.selectByExample(example);
        //
        return list;
    }

    //
    private Attribute[] jsonToAttributes(String attributeJson) {

        //
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(attributeJson, type);

        //
        Attribute[] attributes = new Attribute[1];
        attributes[0] = new Attribute(map.get("key").getBytes(), map.get("valueType").getBytes(), map.get("value").getBytes());
        // TODO 不同的类型

        //        int i = 0;
        //        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        //        while (it.hasNext()) {
        //            Map.Entry<String, String> pair = it.next();
        //            attributes[i] = new Attribute(pair.getKey().getBytes(), "String".getBytes(), pair.getValue().getBytes());
        //            i++;
        //        }
        //
        return attributes;
    }

    //
    private OntidPojo getPojoByOntid(String ontid) {
        //
        Example example = new Example(ActionOntid.class);
        example.createCriteria().andCondition("ontid=", ontid);
        ActionOntid record = actionOntidMapper.selectOneByExample(example);
        //
        String keystore = record.getKeystore();
        OntidPojo ontidPojo = gson.fromJson(keystore, OntidPojo.class);
        return ontidPojo;
    }

    //
    public com.github.ontio.account.Account getAccountByOntidAndPassword(String ontid,
                                                                         String ontidPassword) throws Exception {
        //
        OntidPojo controlPojo = getPojoByOntid(ontid);
        //
        IdentityInfo identityInfo = chainService.ontSdk.getWalletMgr().getIdentityInfo(ontid, ontidPassword, controlPojo.getControls().get(0).getSalt());
        //
        String priKey = com.github.ontio.account.Account.getGcmDecodedPrivateKey(identityInfo.encryptedPrikey,
                                                                                 ontidPassword,
                                                                                 controlPojo.getControls().get(0).getAddress(),
                                                                                 controlPojo.getControls().get(0).getSalt(),
                                                                                 16384,
                                                                                 SignatureScheme.SHA256WITHECDSA);
        // System.out.println(priKey);

        //
        com.github.ontio.account.Account account = new com.github.ontio.account.Account(Helper.hexToBytes(priKey), SignatureScheme.SHA256WITHECDSA);
        return account;
    }

    public void saveToLocal(String ontid,
                            String control,
                            String txhash,
                            Integer type,
                            String detail) {
        // 写入本地表
        Action record = new Action();
        record.setOntid(ontid);
        record.setControl(control);
        record.setTxhash(txhash);
        record.setType(type);
        record.setDetail(detail);
        record.setCreateTime(new Date());
        //
        actionMapper.insertSelective(record);
    }
}
