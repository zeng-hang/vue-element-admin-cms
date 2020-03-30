# vue-element-admin-cms
基于vue-element-admin搭建的一款后台管理系统，版本号4.2.1

## 框架选择
* 后台使用spring-boot搭建，
* 数据持久化使用mybatis-puls
* 权限验证使用shiro

## 目录结构
### cms
```
|-- src                         # java文件的根目录
    |-- java                    # 所有的java文件
        |-- common              # 全局通用工具包
            |-- annotation      # 全局注解
            |-- aspect          # 全局注解实现
            |-- exception       # 全局异常处理
            |-- utlis           # 全局工具类
            |-- xss             # xss处理类
        |-- config              # 全局配置文件
        |-- sys                 # 系统管理相关
            |-- controller      # 控制层
            |-- dao             # 数据持久层
            |-- entity          # 实体层
            |-- service         # 
    |-- resources               # 所有资源文件
        |-- mapper              # mybatis相关的mapper文件
            |-- sys             # 系统管理的mapper文件
        application.yml         # 全局配置文件
        application-dev.yml     # 开发环境配置
        application-prod.yml    # 生产环境配置
|-- vue-element-admin           # vue-element-admin的所有相关文件，目录结构可参考官网
```