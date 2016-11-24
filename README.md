# Codo-API

Web API of Code App implement in JSP

## How to use

Please visit [Wiki](https://github.com/ZERR2AC/Codo-API/wiki)

## Database

### Table

#### user

| id      | username | password                            |
| ------- | -------- | ----------------------------------- |
| user id | username | sha256 (username + password + salt) |

#### token

| id   | user_id | token                        |
| ---- | ------- | ---------------------------- |
| id   | user id | md5 (username + time + salt) |

#### channel

| id         | name         | creater_id |
| ---------- | ------------ | ---------- |
| channel id | channel name | creater id |

#### user_channel

| id   | user_id | channel_id |
| ---- | ------- | ---------- |
|      |         |            |

#### reminder

| id   | title | content | due  | priority | channel_id |
| ---- | ----- | ------- | ---- | -------- | ---------- |
|      |       |         |      |          |            |

#### user_reminder

| id   | user_id | reminder_id | remark | state |
| ---- | ------- | ----------- | ------ | ----- |
|      |         |             |        |       |