
# 注意！表名称可能不一样，运行之前，要对应修改一下！
ALTER TABLE `tbl_contract_201904221200` ADD `status` INT NOT NULL DEFAULT '0' COMMENT '0-未删除；1-已删除；' AFTER `update_time`;