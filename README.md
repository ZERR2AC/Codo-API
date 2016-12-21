# Codo-API

Web API of Code App implement in JSP

## Deployment

### CORS

在 Web 端向后端 API 请求的时候，由于域名不一致，会有 XMLHttpRequest 的 CORS 问题。CORS 问题来源于浏览器出于安全考虑，会限制脚本中发起的跨站请求。比如，使用 XMLHttpRequest 对象发起 HTTP 请求就必须遵守同源策略。具体而言，Web 应用程序能且只能使用 XMLHttpRequest 对象向其加载的源域名发起 HTTP 请求，而不能向任何其它域名发起请求。

参考了[关于 W3C 的 CORS 标准](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS)之后，在响应中加入了 `Access-Control-Allow-Origin` 等 header。

由于 CORS 分为简单请求和非简单请求，需要分开判断。因为浏览器是根据服务端返回的 HTTP Response 来判断是否接受这一次请求，此时 Request 早已发出去，如果是 PUT 和 DELETE 这些请求，很可能服务器已对请求进行处理了，而浏览器拒绝了，会造成不一致的情况。为了解决这些非简单请求造成的影响，浏览器会使用 HTTP 中 OPTIONS 的方法，询问一下服务器是否允许某个方法。

这里附上我在 Nginx 的配置中加入的项目。

```nginx
location / {
    if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Origin' '*';
        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, DELETE';
        add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
        add_header 'Access-Control-Max-Age' 1728000;
        add_header 'Content-Type' 'text/plain charset=UTF-8';
        add_header 'Content-Length' 0;
        return 204;
    }
    add_header 'Access-Control-Allow-Origin' '*';
    add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, DELETE';
    add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
}
```

## How to use

![](http://ww1.sinaimg.cn/large/006tNc79jw1faw4j97a2kj30s80dw3zo.jpg)

上述为 Codo API 的 顶层用例图，所有操作是基于用户的。

访客通过 `/user/login`  使用账号密码登录，登录成功后可以获得 token，下面的 API 访问均需要附带 token 验证用户身份。

用户可以创建 Channle，创建者可以修改自己 Channel 的信息；用户可以获取所有 Channel 的信息，加入或退出 Channle，加入后可以接收到 Channel 的推送。

Channel 的创建者可以在 Channel 中创建 Reminder，该 Reminder 会推送到当前 Channel 所有订阅者中，创建者后续对 Reminder 的 content due priority 修改会同步到所有订阅者的 Reminder 中；订阅者可以给 Reminder 添加 remark 和 修改 state。

More information please visit [Wiki](https://github.com/ZERR2AC/Codo-API/wiki)

## UML

![](https://static.32ph.com/upload-pic/diagram2.svg)

### APIController

Controller 的基类，封装了一系列验证参数的函数，默认会产生 200 OK 的 JSON 响应。

### AuthController

继承 APIController，会自动验证 Token，通过 Token 获得当前用户。不合法的 Token 会抛出异常，并自动产生对应的 JSON Response。

### RestURLIdController

继承 AuthController，处理形如 `model/{id}` 这类型的 URL，会自动判断资源 ID 的合法性，不合法的资源访问会抛出异常，并自动产生对应的 JSON Response。

### Model

项目中主要有 User，Channel 和 Reminder 三类 Model，部分实现了 ORM，将一些数据库操作封装在类方法中。

### Router

通过继承 HttpServlet，重写 doGet doPost 等函数，在函数中将请求和响应转发中对应 Controller 中，完成路由的部分。

### Util

Database 中封装了对 MySQL 数据库的 CURD 操作。

Json 中封装了对 Gson 的单例操作，将 Object 转化为 JSON 字符串。

Redis 中封装了 Redis 数据库的一个连接池，使用 Redis 是考虑到了查询 token 和 token 有效期的问题。Redis Key Value 的内存数据库适合储存热数据，降低每次请求对 MySQL 数据库查询的压力。而且由于 Redis 的特效，可以方便地对 token 设置有效期，使失效的 token 自动从数据库中去除。

CONSTANT 中记录了一些状态码等常量。

## Database

### Table

![](https://static.32ph.com/upload-pic/diagram3.svg)

#### user

user 表记录用户名和密码，其中密码使用 `SHA256(username + password + SALT)`，这样保证即使密码相同，不同用户的数据库的 password 字段也不一样，使用加盐和 SHA256 摘要可以防止被拖库后被用彩虹表碰撞。

#### token

生成 token 采用  `SHA256(username + Timestamp + SALT)` 生成，同上，保证即使 token 泄漏之后密码不会被猜测到。

#### user_channel & user_reminder

记录用户加入 channel / reminder 的信息，通过与 reminder / channel INNER JOIN 可以得到完整的信息。

## todo

### Filter

在这次的项目中，验证 URL 和 Token 的部分，我自己重新做了一次轮子，实现得不够优雅。后来看 Servlet 的文档发现可以使用 Servlet 过滤器，即 Filter 类。通过继承 Filter 去处理可以使得程序更加具有拓展性。

但是由于时间等各种问题，没有对项目进行重构。

### Auth

由于 API 也提供给 Android 上的 App 使用，为了提高兼容性，没有使用 JSP 自带的 Session 进行认证。除了基于 Session 常见的还有 JSON Web Token 和  OAuth。

我们的设计主要参考了 [JSON Web Token](https://tools.ietf.org/html/rfc7519)，但考虑到前端后端工作量，以及我们这只是一份大作业而不是工程项目，我们去掉了鉴权的部分，简化了流程。



