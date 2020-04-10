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
            |-- service         # service层
    |-- resources               # 所有资源文件
        |-- mapper              # mybatis相关的mapper文件
            |-- sys             # 系统管理的mapper文件
        application.yml         # 全局配置文件
        application-dev.yml     # 开发环境配置
        application-prod.yml    # 生产环境配置
|-- vue-element-admin           # vue-element-admin的所有相关文件，目录结构可参考官网
```
**在cms项目包下面的目录看起来可能会很凌乱，因为同时打开java项目和前端页面里面的目录特别多，
但是本着前后端分离原则的话可以选择将`vue-element-admin`移出该项目包，
因为我是个人在做这个项目管理，方便区分项目文件，所以将前后端文件都放在一个项目包中，
如果是团队，区分前后端的话，建议将项目文件进行区分，这并不会影响项目的开发。  
  
关于项目的包名为什么写的这么奇怪，不是`com`开头的，
因为是个人项目，不是团队项目，所以以`pers`开头，后面的也是类似一堆也是类似原因，
看不习惯没关系，使用`idea`进行修改，会帮你一键修改好。**

### 跨域配置
在开发环境中，使用的是代理进行跨域配置，配置文件在`vue-element-vue`目录下面的`vue.config.js`文件中，配置代码如下
```javascript
    devServer: {
        port: port,
        overlay: {
            warnings: false,
            errors: true
        },
        proxy: {
            // change xxx-api/login => mock/login
            // detail: https://cli.vuejs.org/config/#devserver-proxy
            [process.env.VUE_APP_BASE_API]: {
                target: process.env.VUE_APP_SERVICE_URL,
                changeOrigin: true,
                pathRewrite: {
                    ['^' + process.env.VUE_APP_BASE_API]: ''
                }
            }
        }
    }
```
这里的配置完全不用修改，所以不过多的介绍，详细请前往：[vue-cli代理配置](https://cli.vuejs.org/config/#devserver-proxy)   
需要修改的是`.evn.*`文件中的，`VUE_APP_BASE_API`和`VUE_APP_SERVICE_URL`属性  
`VUE_APP_BASE_API`是项目的`contentPath`  
`VUE_APP_SERVICE_URL`是项目的部署地址  

***

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

#### 字典管理相关介绍
- 对于字典管理这方面的功能，在我的项目中是没有使用到的，但是这个功能可以说在很多系统当中使用到了。
- 但是我个人不是很喜欢这个功能的，因为这个功能时属于一个约定性的功能，我一个人做的项目并没有那么强的约束，可以说是自己怎么开心怎么来就行了。
- 个人在项目开发的过程中，发现数据字典这个功能存在滥用的情况，所以更不喜欢这个功能了，但是这个功能如果使用得当还是有很大的价值的。
- 我也不知道为啥有人会觉得数据字典的存在，就想一个万金油一样的功能，什么东西都往里面加，结果弄得数据字典变成一个垃圾桶一样的功能，数据表越来越庞大，但是很多字段对于其他的数据来说毫无意义。
- 这个功能做的简单的意义在于，如果你不想要，方便去掉，如果你觉得我的设计还行，可以在我的基础上面进行改造。
- 至于这个项目上面的字典管理只是一个很简单的增删改查的一个功能，如果不喜欢我的自己可以改成自己喜欢的就好。
