package ntu.service_centric.global_dorm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

    private static final Gson gson = new Gson();
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Serialize an object into a JSON string.
     *
     * @param object Object to serialize.
     * @return JSON string.
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Serialize an object into a beautified JSON string.
     *
     * @param object Object to serialize.
     * @return Beautified JSON string.
     */
    public static String toPrettyJson(Object object) {
        return prettyGson.toJson(object);
    }

    /**
     * Returns the serialized JSON string for a given object.
     *
     * @param object Object to serialize.
     * @param pretty Whether to return prettified JSON.
     * @return JSON string.
     */
    public static String getJSON(Object object, boolean pretty) {
        if (pretty) {
            return toPrettyJson(object);
        }
        return toJson(object);
    }
}
