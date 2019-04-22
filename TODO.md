交易落账确认，设confirmFlag，，单独的表
tx是本地生成的，还是链上返回的
不需要replay机制
未落账，返回success，会引起误解

本地有ddo，则不去链上取，
但要保证ddo最新

删除control？

password遗失

要增加 查询 增加和删除属性的 历史交易记录
属性要按时间顺序展现的话？
分页

control增加了，本地的keystore要不要相应更新

mybatis-generator自动生成生成其它索引？

检查username是否已注册
其它unique字段也要检查
更新属性时，先检查是否在表中


keystore 对应的是 control部分 还是 整个？


不写入钱包文件的话，update属性
```json
{
    "result": "",
    "error": 59000,
    "action": "updateOntidAttribute",
    "desc": "Other Error,Account null",
    "version": "1.0.0"
}
```

接口外的请求，排除

attribute的增删，要考虑和链的同步

控制对同一个ontid，当还有增删属性的tx未确认时，不允许操作？


sql 语句报错的处理


filehash unique
链上的key value 不会做判断


优化http请求，pool，单例

ontid等格式统一校验模块

查询txid是否已确认，单独接口

refresh Token刷新


批量存证，batch insert

userontid
companyontid

存证时，ontid要先注册吗

passphrase

报错时，重发，切换

司法链 accessToken