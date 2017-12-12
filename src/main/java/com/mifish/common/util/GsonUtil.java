package com.mifish.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-12 22:33
 */
public final class GsonUtil {

    /***gson*/
    private final static Gson GSON;

    private final static JsonParser JSON_PARSER = new JsonParser();

    static {
        GSON = new GsonBuilder()
                .enableComplexMapKeySerialization()
//                .serializeNulls()
                .create();
    }

    /**
     * toJSON,
     *
     * @param obj
     * @return
     */
    public static String toJSON(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * parse2Obj
     * <p>
     * clazz只能支持普通对象，当为list，map时，并且对象比较复杂的时候，不一定能够转成原生的对象
     *
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse2Obj(String message, Class<T> clazz) {
        T obj = GSON.fromJson(message, clazz);
        return clazz.cast(obj);
    }

    /**
     * parse2List
     *
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parse2List(String message, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        JsonArray array = JSON_PARSER.parse(message).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(GSON.fromJson(elem, clazz));
        }
        return list;
    }

    /**
     * parse2Map
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> parse2Map(String message, Class<T> clazz) {
        Map<String, T> map = new HashMap<>(16);
        JsonObject jsonObject = JSON_PARSER.parse(message).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), GSON.fromJson(entry.getValue(), clazz));
        }
        return map;
    }

    private GsonUtil(){

    }
}
