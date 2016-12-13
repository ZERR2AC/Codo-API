package pub.codo.Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by terrychan on 10/12/2016.
 */
public class Redis {
    private static JedisPool jedisPool = new JedisPool(CONSTANT.REDIS.HOST, CONSTANT.REDIS.PORT);

    private Redis() {
    }

    public static String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public static Long getExTime(String key) {
        Jedis jedis = jedisPool.getResource();
        Long time = jedis.ttl(key);
        jedis.close();
        return time;
    }

    public static boolean set(String key, String value) {
        if (get(key) != null) return false;
        else {
            Jedis jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.close();
            return true;
        }
    }

    public static boolean setex(String key, String value, int exTime) {
        if (get(key) != null) return false;
        else {
            Jedis jedis = jedisPool.getResource();
            jedis.setex(key, exTime, value);
            jedis.close();
            return true;
        }
    }
}
