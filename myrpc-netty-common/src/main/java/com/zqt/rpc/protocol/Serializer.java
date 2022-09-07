package com.zqt.rpc.protocol;

import com.google.gson.*;
import com.zqt.rpc.message.Message;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author zqtstart
 * @create 2022-09-06 11:45
 */
public interface Serializer {

    public <T> byte[] serialize(T object);

    public <T> T deserialize(Class<T> clazz, byte[] bytes);

    enum Algorithm implements Serializer{
        java{
            @Override
            public <T> byte[] serialize(T object) {
                byte[] bytes = null;
                try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos)){
                    oos.writeObject(object);
                    bytes = bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bytes;
            }

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                Message message = null;
                try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)){
                    message = (Message) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return (T) message;
            }
        },

        gson{
            @Override
            public <T> byte[] serialize(T object) {
//                Gson gson = new Gson();
                Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
                String json = gson.toJson(object);
                return json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
//                Gson gson = new Gson();
                Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
                return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
            }
        }
    }

    class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>>{

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }
    }

}
