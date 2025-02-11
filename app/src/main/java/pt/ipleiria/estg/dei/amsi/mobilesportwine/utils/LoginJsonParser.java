package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginJsonParser {
    public static String parseLoginToken(String response) {
        String token = "";
        try {
            JSONObject getUser = new JSONObject(response);
            token = getUser.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static int parseUserId(String response) {
        int userId = -1; // Valor padrão caso não consiga pegar o ID
        try {
            JSONObject getUser = new JSONObject(response);
            userId = getUser.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userId;
    }
}

