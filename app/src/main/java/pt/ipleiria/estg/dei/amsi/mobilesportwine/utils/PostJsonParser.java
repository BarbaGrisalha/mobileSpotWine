package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Post;

public class PostJsonParser {
    public static ArrayList<Post> parserJsonPosts(JSONArray response) {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject postObj = response.getJSONObject(i);
                int id = postObj.getInt("id");
                int userId = postObj.getInt("user_id");
                String title = postObj.getString("title");
                String content = postObj.getString("content");
                String imageUrl = postObj.isNull("image_url") ? null : postObj.getString("image_url");
                String createdAt = postObj.getString("created_at");

                posts.add(new Post(id, title, content, imageUrl, createdAt));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public static Post parserJsonPost(String response){
        Post auxPost = null;

        try{
            JSONObject postObj = new JSONObject(response);
            int id = postObj.getInt("id");
            int userId = postObj.getInt("user_id");
            String title = postObj.getString("title");
            String content = postObj.getString("content");
            String imageUrl = postObj.isNull("image_url") ? null : postObj.getString("image_url");
            String createdAt = postObj.getString("created_at");

            auxPost = new Post(id, title, content, imageUrl, createdAt);

        }catch( JSONException e){
            e.printStackTrace();
        }

        return auxPost;
    }
}
