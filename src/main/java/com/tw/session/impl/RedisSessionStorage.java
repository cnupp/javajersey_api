package com.tw.session.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.session.core.SessionStorage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.util.HashMap;

public class RedisSessionStorage implements SessionStorage {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionStorage.class);

    private static JedisPool jedisPool;
    private static ObjectMapper mapper = new ObjectMapper();

    public RedisSessionStorage(String master) {
        jedisPool = new JedisPool(new JedisPoolConfig(), master);
    }

    public boolean replace(String key, int expireSeconds, Object obj) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        if (obj == null) {
            return true;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.setex(key.getBytes(), expireSeconds, jsonifyObject(obj));
            logger.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + " result:" + result);
            return "OK".equals(result);
        } catch (Exception e) {
            logger.error("UNABLE TO REPLACE KEY: {}", key, e);
        }

        return false;
    }

    public boolean set(String key, int expireSeconds, Object obj) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        if (obj == null) {
            return true;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.setex(key.getBytes(), expireSeconds, jsonifyObject(obj));

            logger.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + "result:" + result);
            return "OK".equals(result);
        } catch (Exception e) {
            logger.error("ERROR: Can not set the object with key: {}", key, e);
        }

        return false;
    }

    public Object get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] item = jedis.get(key.getBytes());

            if (null == item) {
                return null;
            }

            Object obj = parseObjectToJson(item);
            logger.debug("GET A OBJECT: KEY:" + key + " OBJ:" + obj);
            return obj;
        } catch (Exception e) {
            logger.error("UNABLE TO GET REDIS KEY {}", key, e);
        }
        return null;
    }

    public boolean delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            logger.info("REMOVE REDIS KEY: " + key);
            return jedis.del(key) > 0;
        } catch (Exception e) {
            logger.error("UNABLE TO REMOVE REDIS KEY {}", key, e);
        }

        return true;
    }

    public static byte[] serializeObject(Object obj) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(256);
        Serializer serializer = new Serializer();
        try {
            serializer.serialize(obj, byteStream);
            return byteStream.toByteArray();
        } catch (Throwable e) {
            throw new Exception("Failed to serialize object using " +
                    serializer.getClass().getSimpleName(), e);
        }
    }

    public static Object deserializeObject(byte[] b) throws Exception {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
        Serializer serializer = new Serializer();
        try {
            return serializer.deserialize(byteStream);
        } catch (Throwable ex) {
            throw new Exception("Failed to deserialize payload. Is the byte array a result of corresponding serialization for " +
                    serializer.getClass().getSimpleName() + "?", ex);
        }
    }

    public static byte[] jsonifyObject(Object obj) throws Exception {
        return mapper.writeValueAsBytes(obj);
    }

    public static Object parseObjectToJson(byte[] b) throws Exception {
        return mapper.readValue(b, HashMap.class);
    }

    public static class Serializer {
        public void serialize(Object object, OutputStream outputStream) throws IOException {
            if (!(object instanceof Serializable)) {
                throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " +
                        "but received an object of type [" + object.getClass().getName() + "]");
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        }

        public Object deserialize(InputStream inputStream) throws IOException {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                return objectInputStream.readObject();
            } catch (ClassNotFoundException ex) {
                throw new IOException("Failed to deserialize object type", ex);
            }
        }
    }
}

