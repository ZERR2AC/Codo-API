# Codo-API

Web API of Code App implement in JSP

## How to use

Please visit [Wiki](https://github.com/ZERR2AC/Codo-API/wiki)

## Database

### Table

#### user

| id      | username | password                       |
| ------- | -------- | ------------------------------ |
| user id | username | sha256(username+password+salt) |

#### channel

| id         | name         | creater_id |
| ---------- | ------------ | ---------- |
| channel id | channel name | creater id |

#### reminder

#### user_log