package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginJsonParser {
    public static String parseLoginData(String response) {
        String auxUser = "";

        try {

            JSONObject getUser = new JSONObject(response);
            String getToken = getUser.getString("token");

            auxUser = getToken;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return auxUser;
    }
}
