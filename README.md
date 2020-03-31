# vue-element-admin-cms
基于vue-element-admin搭建的一款后台管理系统，版本号4.2.1   
对作者原有的功能没有做任何的删减，部分页面做了一些细微的改动，不影响使用。   
因为需要去掉mock模拟的数据接入后台，后台使用了我自己的想法，权限验证的逻辑做了一些修改。   
***

## 框架选择
* 后台使用spring-boot搭建，
* 数据持久化使用mybatis-puls
* 权限验证使用shiro

## CMS目录结构
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

#### 菜单管理相关介绍  
- 菜单类型：  
  - 目录：理论上只要有子级的都为目录。  
  -  菜单：实际看到的页面。  
  -  按钮：主管权限控制。  
- 关联主菜单： 父级菜单，如果没有父级不选即可。
- 菜单标题：显示在侧边栏和面包屑上的标题。
- 菜单名称：路由中的`name`属性，唯一，最好和页面中的`name`属性一一对应。
- 权限标识：后台使用的权限识别码，多个使用英文逗号分隔。
- 跳转地址：浏览器地址栏中/#/之后的地址，建议填写为实际页面存在的目录地址（省略views目录）。
- 重定向地址：路由中的`redirect`属性，非必填。
- 跳转模板：如果菜单类型为目录，并且是最顶级，该项请选择`基础模板`，否则请选择`空白模板`,这一块其实比较灵活，大家可自行尝试。
- 后面的三个开关自行尝试作用（写文档太累，主要是懒）
***
