# ont-sourcing

## 接口

### 项目方接口

* [创建](#创建)

* [读取](#读取)

* [更新](#更新)


## 接口规范

### 创建

```text
url：/api/v1/contract/company/add
method：POST
```

- 请求：

```json
{
	"ontid":"did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
	"prikey":"6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef",
	"code_addr":"6864a62235279e4c5c3fba004905f30e2157169a"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 项目方ontid    |
| prikey   | String | 项目方私钥    |
| code_addr   | String | 智能合约地址（合约中的地址需与付款账号地址一致）    |

- 响应：

```json
{
    "result": "",
    "error": 0,
    "action": "addCompany",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回ontid，失败返回""     |
| version    | String | 版本号                      |


### 读取

```text
url：/api/v1/contract/company/get
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
| ontid   | String | 项目方ontid    |


- 响应：

```json
{
    "result": {
        "id": 1,
        "ontid": "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
        "prikey": "6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef",
        "codeAddr": "12345679e4c5c3fba004905f30e2157169a",
        "createTime": "2019-04-24T05:27:14.000+0000",
        "updateTime": "2019-04-24T05:29:13.000+0000"
    },
    "error": 0,
    "action": "getCompany",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回ontid，失败返回""     |
| version    | String | 版本号                      |



### 更新

```text
url：/api/v1/contract/company/update
method：POST
```

- 请求：

```json
{
	"ontid":"did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE",
	"prikey":"12345616e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef",
	"code_addr":"12345679e4c5c3fba004905f30e2157169a"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 项目方ontid    |
| prikey   | String | 项目方私钥    |
| code_addr   | String | 智能合约地址（合约中的地址需与付款账号地址一致）    |

- 响应：

```json
{
    "result": "",
    "error": 0,
    "action": "updateCompany",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回ontid，失败返回""     |
| version    | String | 版本号                      |



