# Codo-API

Web API of Code App implement in JSP

## How to use

Please visit [Wiki](https://github.com/ZERR2AC/Codo-API/wiki)

## Database

### Table

![](http://ww1.sinaimg.cn/large/006y8mN6jw1fa75fhcfpyj30ov0en76p.jpg)

#### user

| id      | username | password                            |
| ------- | -------- | ----------------------------------- |
| user id | username | sha256 (username + password + salt) |

#### token

| id       | user_id | token                        |
| -------- | ------- | ---------------------------- |
| token id | user id | md5 (username + time + salt) |

#### channel

| id         | name         | creater_id |
| ---------- | ------------ | ---------- |
| channel id | channel name | creater id |

#### user_channel

| id   | user_id | channel_id |
| ---- | ------- | ---------- |
|      | user id | channel id |

#### reminder

| id   | title          | creater_id | content | due  | priority | channel_id | type |
| ---- | -------------- | ---------- | ------- | ---- | -------- | ---------- | ---- |
|      | reminder title |            |         |      |          |            |      |

#### user_reminder

| id   | user_id | reminder_id | remark | state |
| ---- | ------- | ----------- | ------ | ----- |
|      |         |             |        |       |

