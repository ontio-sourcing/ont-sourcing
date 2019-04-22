package com.ontology.sourcing.service.utils;

import com.ontology.sourcing.dao.contract.ContractTypes;
import com.ontology.sourcing.dao.ddo.ActionOntid;
import com.ontology.sourcing.mapper.ddo.ActionOntidMapper;
import com.ontology.sourcing.service.oauth.OAuthService;
import com.ontology.sourcing.utils.exp.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ValidateService {

    //
    private ActionOntidMapper actionOntidMapper;

    //
    private OAuthService oauthService;

    //
    @Autowired
    public ValidateService(ActionOntidMapper actionOntidMapper, OAuthService oauthService) {
        //
        this.actionOntidMapper = actionOntidMapper;
        //
        this.oauthService = oauthService;
    }

    // TODO 只有ontid账户托管才需要检查
    public void isExistedOntid(String ontid) throws Exception {
        ActionOntid actionOntidRecord = actionOntidMapper.findByOntid(ontid);
        if (actionOntidRecord == null) {
            throw new Exception(ErrorCode.ONTID_NOT_EXIST.getMessage());
        }
    }

    private void validateOntid(String ontid) throws Exception {
        //
        if (StringUtils.isEmpty(ontid))
            throw new Exception("ontid is empty.");
        //
        if (ontid.length() != 42)
            throw new Exception("ontid length should be 42.");
        //
        if (!ontid.toLowerCase().contains("did:ont:A".toLowerCase()))
            throw new Exception("ontid format invalid.");
        //
        // isExistedOntid(ontid);
    }

    private void validateFilehash(String filehash) throws Exception {
        //
        if (StringUtils.isEmpty(filehash))
            throw new Exception("filehash is empty.");
        //
        if (filehash.length() != 64)
            throw new Exception("filehash length should be 64.");
    }

    private void validateTxhash(String txhash) throws Exception {
        //
        if (StringUtils.isEmpty(txhash))
            throw new Exception("txhash is empty.");
        //
        if (txhash.length() != 64)
            throw new Exception("txhash length should be 64.");
    }

    private void validateHash(String hash) throws Exception {
        //
        if (StringUtils.isEmpty(hash))
            throw new Exception("hash is empty.");
        //
        if (hash.length() != 64)
            throw new Exception("hash length should be 64.");
    }

    public void validateParamsKeys(LinkedHashMap<String, Object> obj, Set<String> required) throws Exception {
        Iterator<String> requiredIterator = required.iterator();
        while (requiredIterator.hasNext()) {
            String next = requiredIterator.next();
            if (!obj.containsKey(next))
                throw new Exception(next + " is missing.");
        }
    }

    public void validateParamsValues(LinkedHashMap<String, Object> obj) throws Exception {

        if (obj.containsKey("path_key")) {
            String path_key = (String) obj.get("path_key");
            if (StringUtils.isEmpty(path_key)) {
                throw new Exception("path_key is empty.");
            }
        }

        if (obj.containsKey("username")) {
            String username = (String) obj.get("username");
            //
            if (StringUtils.isEmpty(username)) {
                throw new Exception("username is empty.");
            }
            // TODO is existed
        }

        if (obj.containsKey("password")) {
            String password = (String) obj.get("password");
            if (StringUtils.isEmpty(password)) {
                throw new Exception("password is empty.");
            }
        }

        if (obj.containsKey("passphrase")) {
            String passphrase = (String) obj.get("passphrase");
            if (StringUtils.isEmpty(passphrase)) {
                throw new Exception("passphrase is empty.");
            }
        }

        if (obj.containsKey("attribute")) {
            String attribute = (String) obj.get("attribute").toString();
            if (StringUtils.isEmpty(attribute)) {
                throw new Exception("attribute is empty.");
            }
        }

        if (obj.containsKey("controlPassword")) {
            String controlPassword = (String) obj.get("controlPassword");
            if (StringUtils.isEmpty(controlPassword)) {
                throw new Exception("controlPassword is empty.");
            }
        }

        // TODO detail

        if (obj.containsKey("type")) {
            String type = (String) obj.get("type");
            if (!ContractTypes.contains(type)) {
                throw new Exception("type " + type + " is incorrect.");
            }
        }

        if (obj.containsKey("filehash")) {
            String filehash = (String) obj.get("filehash");
            validateFilehash(filehash);
        }

        if (obj.containsKey("txhash")) {
            String txhash = (String) obj.get("txhash");
            validateTxhash(txhash);
        }

        if (obj.containsKey("hash")) {
            String hash = (String) obj.get("hash");
            validateHash(hash);
        }

        if (obj.containsKey("ontid")) {
            String ontid = (String) obj.get("ontid");
            validateOntid(ontid);
        }

        if (obj.containsKey("controlOntid")) {
            String controlOntid = (String) obj.get("controlOntid");
            validateOntid(controlOntid);
        }

        if (obj.containsKey("user_ontid")) {
            String user_ontid = (String) obj.get("user_ontid");
            //
            if (!StringUtils.isEmpty(user_ontid)) {
                //
                if (user_ontid.length() != 42)
                    throw new Exception("user_ontid length should be 42.");
                //
                if (!user_ontid.toLowerCase().contains("did:ont:A".toLowerCase()))
                    throw new Exception("user_ontid format invalid.");
            }
        }

        if (obj.containsKey("pageNum")) {
            Integer pageNum = Integer.valueOf(obj.get("pageNum").toString());
            if (StringUtils.isEmpty(pageNum))
                throw new Exception("pageNum not valid.");
        }

        if (obj.containsKey("pageSize")) {
            Integer pageSize = Integer.valueOf(obj.get("pageSize").toString());
            if (StringUtils.isEmpty(pageSize) || pageSize > 10)
                throw new Exception("pageSize not valid.");
        }

        if (obj.containsKey("access_token")) {
            String access_token = (String) obj.get("access_token");
            if (StringUtils.isEmpty(access_token)) {
                throw new Exception("access_token is empty.");
            }
            // 验证 token
            oauthService.verify(access_token);
        }

        // 司法链
        if (obj.containsKey("user_type")) {
            String user_type = (String) obj.get("user_type");
            if (StringUtils.isEmpty(user_type)) {
                throw new Exception("user_type is empty.");
            }
            //
            if (!"PERSON".equals(user_type) && !"ENTERPRISE".equals(user_type)) {
                throw new Exception("user_type is incorrect.");
            }
        }

        if (obj.containsKey("cert_type")) {
            String cert_type = (String) obj.get("cert_type");
            if (StringUtils.isEmpty(cert_type)) {
                throw new Exception("cert_type is empty.");
            }
            //
            if (!"IDENTITY_CARD".equals(cert_type) && !"UNIFIED_SOCIAL_CREDIT_CODE".equals(cert_type) && !"ENTERPRISE_REGISTERED_NUMBER".equals(cert_type)) {
                throw new Exception("cert_type is incorrect.");
            }
        }

        if (obj.containsKey("cert_name")) {
            String cert_name = (String) obj.get("cert_name");
            if (StringUtils.isEmpty(cert_name)) {
                throw new Exception("cert_name is empty.");
            }
        }

        if (obj.containsKey("cert_no")) {
            String cert_no = (String) obj.get("cert_no");
            if (StringUtils.isEmpty(cert_no)) {
                throw new Exception("cert_no is empty.");
            }
        }

    }
}