# ont-sourcing

## 接口

### DDO接口
* [创建ontid](#创建ontid)

* [获取ddo](#获取ddo)

* [更新ontid的属性](#更新ontid的属性)
* [删除ontid的属性](#删除ontid的属性)

* [为ontid添加controller](#为ontid添加controller)

* [通过controller更新ontid的属性](#通过controller更新ontid的属性)
* [通过controller删除ontid的属性](#通过controller删除ontid的属性)

* [获取总条数](#获取总条数)
* [获取历史记录](#获取历史记录)

### 存证接口

* [手机号注册ontid](#手机号注册ontid)

* [验证access_token](#验证access_token)

* [存证](#存证)
* [批量存证](#批量存证)

* [批量存证_点晴定制](#批量存证_点晴定制)


* [根据hash取证](#根据hash取证)
* [根据hash删除存证](#根据hash删除存证)

* [获取存证总条数](#获取存证总条数)
* [获取存证历史记录](#获取存证历史记录)

* [浏览器存证历史记录](#浏览器存证历史记录)
* [浏览器根据hash取证](#浏览器根据hash取证)

### 司法链接口
* [司法链存证](#司法链存证)
* [司法链取证](#司法链取证)

### 附
* [错误码](#错误码)

## 接口规范

### 创建ontid

```text
url：/api/v1/ddo/create
method：POST
```

- 请求：

```json
{
    "username":"entity1",
    "password":"888888"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| username   | String | 设置用户名    |
| password   | String | 设置密码    |

- 响应：

```json
{
    "result": "did:ont:ARMRyKtaukahJgQ4ZTBVhhAByEGPNWip5e",
    "error": 0,
    "action": "createOntid",
    "version": "1.0.0",
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回ontid，失败返回""     |
| version    | String | 版本号                        |（）

### 获取ddo

```text
url：/api/v1/ddo/getddo
method：POST
```

- 请求：

```json
{
    "ontid":"did:ont:ARMRyKtaukahJgQ4ZTBVhhAByEGPNWip5e"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 需要查询的ontid    |

- 响应：

```json
{
    "result": {
        "attributes": [],
        "ontId": "did:ont:ARMRyKtaukahJgQ4ZTBVhhAByEGPNWip5e",
        "owners": [
            {
                "type": "ECDSA",
                "curve": "P256",
                "value": "03c31eea1c24ea949e86437ed6ef44b58e5067d25589bf45c92e751d116e4f04f9",
                "pubKeyId": "did:ont:ARMRyKtaukahJgQ4ZTBVhhAByEGPNWip5e#keys-1"
            }
        ]
    },
    "error": 0,
    "action": "getDDO",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回DDO，失败返回""     |
| version    | String | 版本号                        |（）

### 更新ontid的属性

```text
url：/api/v1/ddo/update/attribute
method：POST
```

- 请求：

```json
{
    "ontid":"did:ont:ARMRyKtaukahJgQ4ZTBVhhAByEGPNWip5e",
    "password":"888888",
    "attribute":{
        "key": "key1",
        "valueType": "String",
        "value": "value1"
    }
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String |   实物ontid    |
| password   | String | 实物ontid密码    |
| attribute   | String | 属性    |

- 响应：

```json
{
    "result": true,
    "error": 0,
    "desc": "SUCCESS",
    "action": "updateOntidAttribute",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true     |
| version    | String | 版本号                        |

### 删除ontid的属性

```text
url：/api/v1/ddo/delete/attribute
method：POST
```

- 请求：

```json
{
    "ontid":"did:ont:AHYEKDyAcCg968yfxLLyQiD9x9UygxKyhW",
    "password":"888888",
    "path_key":"key1"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String |   实物ontid    |
| password   | String | 实物ontid密码    |
| path_key   | String | 要删除的属性键值    |

- 响应：

```json
{
    "result": true,
    "error": 0,
    "desc": "SUCCESS",
    "action": "deleteOntidAttribute",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true     |
| version    | String | 版本号                        |

### 为ontid添加controller

```text
url：/api/v1/ddo/update/control
method：POST
```

- 请求：

```json
{
    "ontid":"did:ont:AHYEKDyAcCg968yfxLLyQiD9x9UygxKyhW",
    "password":"888888",
    "controlOntid":"did:ont:Aa9Y9fg2taqrP6Jt4nJi8u8PHPWggGzsfd"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String |   实物ontid    |
| password   | String | 实物ontid密码    |
| controlOntid   | String | control的ontid    |

- 响应：

```json
{
    "result": true,
    "error": 0,
    "desc": "SUCCESS",
    "action": "updateOntidControl",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| version    | String | 版本号                       |
| result     | String | 成功返回true     |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |

### 通过controller更新ontid的属性

```text
url：/api/v1/ddo/update/attribute/control
method：POST
```

- 请求：

```json
{
    "ontid":"did:ont:AHYEKDyAcCg968yfxLLyQiD9x9UygxKyhW",
    "attribute":{
        "key": "key2",
        "valueType": "String",
        "value": "value2"
    },
    "controlOntid":"did:ont:Aa9Y9fg2taqrP6Jt4nJi8u8PHPWggGzsfd",
    "controlPassword":"999999"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String |   实物ontid    |
| attribute   | String | 属性    |
| controlOntid   | String | control的ontid    |
| controlPassword   | String | control的ontid密码    |

- 响应：

```json
{
    "error": 0,
    "action": "updateOntidAttributeByControl",
    "version": "1.0.0",
    "result": true,
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| version    | String | 版本号                       |
| result     | String | 成功返回true     |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |

### 通过controller删除ontid的属性

```text
url：/api/v1/ddo/delete/attribute/control
method：POST
```

- 请求：

```json
{
    "ontid":"did:ont:AHYEKDyAcCg968yfxLLyQiD9x9UygxKyhW",
    "path_key": "key2",
    "controlOntid":"did:ont:Aa9Y9fg2taqrP6Jt4nJi8u8PHPWggGzsfd",
    "controlPassword":"999999"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| entityOntid   | String |   实物ontid    |
| path_key   | String | 要删除的属性键值    |
| controlOntid   | String | control的ontid    |
| controlPassword   | String | control的ontid密码    |

- 响应：

```json
{
    "error": 0,
    "action": "deleteOntidAttributeByControl",
    "version": "1.0.0",
    "result": true,
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| version    | String | 版本号                       |
| result     | String | 成功返回true     |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |


### 获取总条数

```text
url：/api/v1/ddo/count
method：POST
```

- 请求：

```json
{
	"ontid":"did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 需要查询的ontid    |


- 响应：

```json
{
    "result": 6,
    "error": 0,
    "action": "count",
    "version": "1.0.0",
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回总条数，失败返回""
| version    | String | 版本号                     

### 获取历史记录

```text
url：/api/v1/ddo/history
method：POST
```

- 请求：

```json
{
	"ontid":"did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
	"pageNum": 1,
	"pageSize": 3
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 需要查询的ontid    |
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
            "control": "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
            "txhash": "bb4d3c87344f7e289c9fbbf7bc114a82037573731dd46fb9db3b49cd948ee6a1",
            "type": 0,
            "createTime": "2019-03-26T06:18:08.000+0000",
            "updateTime": null,
            "detail": "{key=key1, valueType=String, value=value1}"
        },
        {
            "ontid": "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
            "control": "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
            "txhash": "0abcbae70882c93f6da0aa6c06b9234d937069033928a537ef659b5039c0ba34",
            "type": 1,
            "createTime": "2019-03-26T06:19:49.000+0000",
            "updateTime": null,
            "detail": "key1"
        },
        {
            "ontid": "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
            "control": "did:ont:AWXLzhgLNgyGPsyj7uUSRfjEKWBY7HcWmx",
            "txhash": "d1bc778ba62b4541bc67f8317447398d28c02eec6fe8db8be288905ea52d620a",
            "type": 0,
            "createTime": "2019-03-26T06:29:12.000+0000",
            "updateTime": null,
            "detail": "{key=key2, valueType=String, value=value2}"
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "getHistory",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号                     


### 手机号注册ontid
```text
url：/api/v1/ontid/create
method：POST
```

- 请求：

```json
{
	"user_phone":"86*18612341234"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| user_phone   | String |  中国大陆手机号（格式必须为86*18612341234）    |


- 响应：

```json
{
    "result": {
        "user_ontid": "did:ont:AXVNLgKBV2xx28yMhX5bc5T96E8uBLZry8"
    },
    "error": 0,
    "version": "1.0.0",
    "desc": "SUCCESS",
    "action": "createOntid"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回ontid，失败返回""     |
| version    | String | 版本号                        |



### 验证access_token

```text
url：/api/v1/token/check
method：POST
```

- 请求：

```json
{
	"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTU5OTMzNjAsImlhdCI6MTU1NTkwNjk2MCwianRpIjoiMTYwY2FkNjNmZTdkNGY5MTk3NGFjZjQzYWNlMzkzNmYiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE5MzE3ODk4ODU2MGQ5NGQ3MTBmZTc2Mzg1ZWE0OWRiMmRjZjczZmU2NjAyYjU0NjI2YWE0MmJmZWYwYTFkYTE0ODI5YWVmYTJjNjNlMTA5N2Y2ZjM0YTJlMTJmOGYwNWNmYzRhZWI3NzlkOWEwMWY2NDY1Y2VjYWM1MzNjYjk5Ng"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |


- 响应：

```json
{
    "result": "2b",
    "error": 0,
    "action": "checkToken",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回2b或2c，优先2b，失败返回""     |
| version    | String | 版本号                        |


### 存证

```text
url：/api/v1/attestation/put
method：POST
```

- 请求：

```json
{
	"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFVRG11NEoyVzF2cUpIRHRMUDhVeEhhdWoyZUtzUUh4dTYiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTcyODM2MTAsImlhdCI6MTU1NzE5NzIxMCwianRpIjoiYmQ5NTZhNGI1YzYxNGYxN2I2YTgxNDkyZDI5NDIyYTQiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDFiMjFkMjg5OGJmYjZlZGQzMmM5ZjY0ZWIxMDA0OGYxNGNkOGE2MTBhYTZmZGNiZTg4OWQyNzI0MjMwZDVjMjk3Y2Q3ZDhjMzlhOGYzZDJkYjE1YzFhMTcxM2Y3OTU4ZjkzYzRjOGI2NmU2ODM5YmFhNjE4NWRjMTlkZjU3YThkYQ",
	"user_ontid":"",
	"filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
	"metadata": {
            "name":"",
            "Title": "",
            "Tags": "",
            "Description":"",
            "Timestamp": "",
            "Location":""
	},
	"context": {
	    "image": ["",""],
	    "text": ["",""]
	},
	"signature":"",
	"type": "TEXT",
	"async": true
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| user_ontid   | String | 空表示自己存证，否则表示被存证    |
| filehash   | String | 文件hash    |
| metadata   | JSON |     |
| context   | JSON |     |
| signature   | String |     |
| type   | String | PDF/TEXT/IMAGE/VIDEO   |
| async   | boolean |  true表示异步，false表示同步，默认为false   |

- 响应：

当 async 为 true 时：
```json
{
    "result": true,
    "error": 0,
    "action": "putAttestation",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

当 async 为 false 时：
```json
{
    "version": "1.0.0",
    "desc": "SUCCESS",
    "result": {
        "txhash": "d6459de184af36ccbc786e19f30ea14961f29b85aa330ea58e07463a73532bac"
    },
    "error": 0,
    "action": "putAttestation"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| result     |  |    |
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| version    | String | 版本号                        |


### 批量存证

```text
url：/api/v1/attestations/put
method：POST
```

- 请求：

```json
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFVRG11NEoyVzF2cUpIRHRMUDhVeEhhdWoyZUtzUUh4dTYiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTc4OTM4MzYsImlhdCI6MTU1NzgwNzQzNiwianRpIjoiNjM3YzY4ODQxMzc1NGMxMGE1ZDM3NDY0MTcwMWQwMTIiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE1YmFjYTI0MTI3ODI2YmI0OWI5YzY1YjU4YTg1Njk5NmRkNjlmMTc1MTM3MGIwM2NhOTQ0ZTY4YzI2NzRjMWU2M2U1MTQ2ODZkYTE3ZWU4OGE2N2E4ZTE1MDQ4ODQzNDZiOTYxMGI4MjhjMzhmNGFkMGNiYTY4MDBhZDVjNDZhNw",
    "user_ontid": "",
    "async": true,
    "filelist": [
        {
            "filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "metadata": {
                    "name":"",
                    "Title": "",
                    "Tags": "",
                    "Description":"",
                    "Timestamp": "",
                    "Location":""
            },
            "context": {
                "image": ["",""],
                "text": ["",""]
            },
            "signature":"",
            "type": "TEXT"
        },
        {
            "filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "metadata": {
                    "name":"",
                    "Title": "",
                    "Tags": "",
                    "Description":"",
                    "Timestamp": "",
                    "Location":""
            },
            "context": {
                "image": ["",""],
                "text": ["",""]
            },
            "signature":"",
            "type": "TEXT"
        }
    ]
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token|
| user_ontid   | String | 空表示自己存证，否则表示被存证    |
| filelist   | String | 批量文件(总数不能超过30条)    |
| async   | boolean |  true表示异步，false表示同步，默认为false   |


- 响应：

当 async 为 true 时：
```json
{
    "result": true,
    "error": 0,
    "action": "putAttestations",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```


当 async 为 false 时：
```json
{
    "result": [
        {
            "txhash": "58eb1b7414d51988899dbaf54ff891aca55eb7ba2f4a5b5008af18a874187d02"
        },
        {
            "txhash": "6cada409428b2d7f4e2186a2a95cd0bdbe82e643ff697b4077129438da0b2e9b"
        }
    ],
    "error": 0,
    "action": "putAttestations",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```


| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     |  |    |
| version    | String | 版本号                        |


### 批量存证_点晴定制

```text
url：/api/v1/attestations/put/custom
method：POST
```

- 请求：

```json
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTU5OTMzNjAsImlhdCI6MTU1NTkwNjk2MCwianRpIjoiMTYwY2FkNjNmZTdkNGY5MTk3NGFjZjQzYWNlMzkzNmYiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE5MzE3ODk4ODU2MGQ5NGQ3MTBmZTc2Mzg1ZWE0OWRiMmRjZjczZmU2NjAyYjU0NjI2YWE0MmJmZWYwYTFkYTE0ODI5YWVmYTJjNjNlMTA5N2Y2ZjM0YTJlMTJmOGYwNWNmYzRhZWI3NzlkOWEwMWY2NDY1Y2VjYWM1MzNjYjk5Ng",
    "user_ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
    "filelist": [
        {
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "type": "INDEX",
            "detail": [
                  {
                      "textLine": [
                          "2d校园",
                          "文本",
                          " 2d校园",
                          "文本",
                          "2d校园",
                          "文本"
                      ],
                      "imageList": [
                          "56e481f7a93a924813440bee96b68f0742b014ed426b7535ad35d072984b2c0f",
                          "56e481f7a93a924813440bee96b68f0742b014ed426b7535ad35d072984b2c0f",
                          "56e481f7a93a924813440bee96b68f0742b014ed426b7535ad35d072984b2c0f"
                      ]
                  }
            ]
        },
        {
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888",
            "type": "IMAGE",
            "detail": [
                {
                    "imgUrl": "http://...."
                }
            ]
        },
        {
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "type": "IMAGE",
            "detail": [
                {
                    "imgUrl": "http://...."
                }
            ]
        },
        {
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000",
            "type": "IMAGE",
            "detail": [
                {
                    "imgUrl": "http://...."
                }
            ]
        }
    ]
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token|
| user_ontid   | String | 空表示自己存证，否则表示被存证    |
| filelist   | String | 批量文件(总数不能超过30条)    |
| type   | String | INDEX/PDF/TEXT/IMAGE/VIDEO   |

- 响应：

```json
{
    "result": true,
    "error": 0,
    "action": "putAttestationsCustom",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true，失败返回""     |
| version    | String | 版本号                        |


### 根据hash取证

```text
url：/api/v1/contract/hash
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"hash":"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:57.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "ee973d13c6ed2d8c7391223b4fb6f5c785f402d81d41b02ab7590113cbb00752",
            "createTime": "2019-04-22T07:32:57.000+0000",
            "updateTime": null,
            "height": 1621684
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY1234",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:24.000+0000",
            "timestampSign": "960ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "1ab4b5b2c6c89b4f1a553b7aef30c3f3ef203a323d23cd383261cc6d0df73870",
            "createTime": "2019-04-22T07:32:25.000+0000",
            "updateTime": null,
            "height": 1621682
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T04:22:55.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "0437084a4f6204aad88fa1507fc13a44f83cecf44fc925692f9bc43f23e52fc3",
            "createTime": "2019-04-22T04:22:57.000+0000",
            "updateTime": null,
            "height": 1621275
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "selectByOntidAndHash",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号                        |


### 根据hash删除存证

```text
url：/api/v1/contract/hash
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"hash":"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "version": "1.0.0",
    "error": 0,
    "action": "deleteByOntidAndHash",
    "result": true,
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true     |
| version    | String | 版本号                        |



### 获取存证总条数

```text
url：/api/v1/contract/count
method：POST
```

- 请求：

```json
{
		"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 需要查询的ontid    |
| access_token   | String | access_token    |



- 响应：

```json
{
    "result": 6,
    "error": 0,
    "desc": "SUCCESS",
    "action": "count",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回总条数，失败返回""
| version    | String | 版本号                     

### 获取存证历史记录

```text
url：/api/v1/contract/history
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTU5OTMzNjAsImlhdCI6MTU1NTkwNjk2MCwianRpIjoiMTYwY2FkNjNmZTdkNGY5MTk3NGFjZjQzYWNlMzkzNmYiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE5MzE3ODk4ODU2MGQ5NGQ3MTBmZTc2Mzg1ZWE0OWRiMmRjZjczZmU2NjAyYjU0NjI2YWE0MmJmZWYwYTFkYTE0ODI5YWVmYTJjNjNlMTA5N2Y2ZjM0YTJlMTJmOGYwNWNmYzRhZWI3NzlkOWEwMWY2NDY1Y2VjYWM1MzNjYjk5Ng",
	"pageNum": 1,
	"pageSize": 3,
	"type":"INDEX"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |
| type   | String | INDEX/TEXT/IMAGE/VIDEO，空表示所有类型   |


- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"name\":\"img1\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888\",\"message\":\"\"},{\"name\":\"img2\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999\",\"message\":\"\"},{\"name\":\"img3\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000\",\"message\":\"\"}]",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:50:45.000+0000",
            "timestampSign": "950ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "261944dfe5f5e83cac5d9b4c1065f508d7750e66adec85d12cb7415ef5cc1d3a",
            "createTime": "2019-04-22T07:50:46.000+0000",
            "updateTime": null,
            "height": 1621724
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "null",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:47:22.000+0000",
            "timestampSign": "950ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "99c240e7860a6016dd38de4d5fc73ea6ed533c76007a4b314741259b54f0937a",
            "createTime": "2019-04-22T07:47:23.000+0000",
            "updateTime": null,
            "height": 1621714
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "null",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:42:54.000+0000",
            "timestampSign": "960ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "4a70100fa6c5ff6b2b5484493c2c147861772821c50df27db28b01b13bc3a593",
            "createTime": "2019-04-22T07:42:55.000+0000",
            "updateTime": null,
            "height": 1621704
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "getExplorerHistory",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号                     


### 浏览器存证历史记录

```text
url：/api/v1/contract/explorer
method：POST
```

- 请求：

```json
{
    "pageNum": 1,
    "pageSize": 3
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-24T09:22:35.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "7116936d9992ce4cbb7f3531d4e3e1807201cc6163d9d5958e375f27d604889a",
            "createTime": "2019-04-24T09:22:35.000+0000",
            "updateTime": null,
            "height": 1628962
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"url\":\"http://....\"}]",
            "type": "IMAGE",
            "timestamp": "2019-04-23T03:49:20.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000",
            "txhash": "ef12912c68d20812acb08a0ab6a0809c85f95ebef1bd8315a3e5f891e3d12688",
            "createTime": "2019-04-23T03:49:22.000+0000",
            "updateTime": null,
            "height": 1624385
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"name\":\"img1\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888\",\"message\":\"\"},{\"name\":\"img2\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999\",\"message\":\"\"},{\"name\":\"img3\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000\",\"message\":\"\"}]",
            "type": "INDEX",
            "timestamp": "2019-04-23T03:49:20.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "89e9ba952dc618229f7ab575c5c85863783fad26a1e65790be843481436cfe07",
            "createTime": "2019-04-23T03:49:21.000+0000",
            "updateTime": null,
            "height": 1624385
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "version": "1.0.0",
    "action": "getExplorer"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号              


### 浏览器根据hash取证

```text
url：/api/v1/contract/explorer/hash
method：POST
```

- 请求：

```json
{
	"hash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:57.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "ee973d13c6ed2d8c7391223b4fb6f5c785f402d81d41b02ab7590113cbb00752",
            "createTime": "2019-04-22T07:32:57.000+0000",
            "updateTime": null,
            "height": 1621684
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY1234",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:24.000+0000",
            "timestampSign": "960ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "1ab4b5b2c6c89b4f1a553b7aef30c3f3ef203a323d23cd383261cc6d0df73870",
            "createTime": "2019-04-22T07:32:25.000+0000",
            "updateTime": null,
            "height": 1621682
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T04:22:55.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "0437084a4f6204aad88fa1507fc13a44f83cecf44fc925692f9bc43f23e52fc3",
            "createTime": "2019-04-22T04:22:57.000+0000",
            "updateTime": null,
            "height": 1621275
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "selectByOntidAndHash",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号                        |
  
       

###  司法链存证

```text
url：/api/v1/sfl/put
method：POST
```

- 请求：

```json
{
    "userType": "PERSON",
    "certType": "IDENTITY_CARD",
    "certName": "刘猛",
    "certNo": "412827199405182010",
    "filehash": "33338348b531a08f4cb80b3ccece79ff0b19c0364e0276b6511045b071d35a5b"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| userType   | String | 用户类型    |
| certType   | String | 证件类型    |
| certName   | String | 证件姓名    |
| certNo   | String | 证件号码    |
| filehash   | String | 文件哈希    |

- 响应：

```json
{
    "result": true,
    "error": 0,
    "action": "putSFL",
    "version": "1.0.0",
    "desc": "SUCCESS"
}
```

### 司法链取证

```text
url：/api/v1/sfl/get
method：POST
```

- 请求：

```json
{
    "certNo": "412827199405182010",
    "pageNum": 1,
    "pageSize": 3
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| certNo   | String | 证件号码    |
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |

- 响应：

```json
{
    "result": [
        {
            "id": 4,
            "certNo": "412827199405182010",
            "filehash": "33338348b531a08f4cb80b3ccece79ff0b19c0364e0276b6511045b071d35a5b",
            "txhash": "02c59f61d43aef48acbeeae29a652b39c5ef488f59b70ad8323a8771f59bfdc1",
            "certUrl": "http://colima-oss-pro.oss-cn-hangzhou.aliyuncs.com/78943305055427.pdf?Expires=1555670383&OSSAccessKeyId=LTAIy0OdPPzHygy4&Signature=KY8Tgx00C0Ak8qNzTwF7kZQRSFY%3D",
            "confirm": 1,
            "createTime": "2019-04-19T10:37:43.000+0000",
            "updateTime": "2019-04-19T10:38:14.000+0000"
        },
        {
            "id": 3,
            "certNo": "412827199405182010",
            "filehash": "22228348b531a08f4cb80b3ccece79ff0b19c0364e0276b6511045b071d35a5b",
            "txhash": "a75352ab3e88ab70359820f8256733f454b88bcabf0a29bad4a45a0b7761c242",
            "certUrl": "http://colima-oss-pro.oss-cn-hangzhou.aliyuncs.com/78779419452531.pdf?Expires=1555670221&OSSAccessKeyId=LTAIy0OdPPzHygy4&Signature=ckaCCzMDWD3oWlDDVFOHJgskLgc%3D",
            "confirm": null,
            "createTime": "2019-04-19T10:35:02.000+0000",
            "updateTime": null
        },
        {
            "id": 2,
            "certNo": "412827199405182010",
            "filehash": "11118348b531a08f4cb80b3ccece79ff0b19c0364e0276b6511045b071d35a5b",
            "txhash": "66d51a49b9f6cb7159d4ac3c5c6e1384382234b5d393329eb07d12806e1386d1",
            "certUrl": "http://colima-oss-pro.oss-cn-hangzhou.aliyuncs.com/78637363155528.pdf?Expires=1555670077&OSSAccessKeyId=LTAIy0OdPPzHygy4&Signature=7%2FGiUSp29P6GjKabTdWUYpuXu%2FI%3D",
            "confirm": 1,
            "createTime": "2019-04-19T10:32:37.000+0000",
            "updateTime": "2019-04-19T10:32:43.000+0000"
        }
    ],
    "error": 0,
    "action": "getSFL",
    "version": "1.0.0",
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号  


## 错误码

| 返回代码  | 描述信息   | 备注                   |
|:-----------|:-------|:------------------------------|
| 0      | SUCCESS | 成功 |
| 61001      | INVALID_PARAMS | 参数错误 |
| 71001      | ONTID_EXIST | ontid错误 |
| 71002      | ONTID_NOT_EXIST | ontid错误 |
| 80001      | BLOCKCHAIN_ERROR | 本体链错误 |
| 90001      | SFL_ERROR | 司法链错误 |
| 100000      | INTERNAL_SERVER_ERROR | 服务器内部错误 |

