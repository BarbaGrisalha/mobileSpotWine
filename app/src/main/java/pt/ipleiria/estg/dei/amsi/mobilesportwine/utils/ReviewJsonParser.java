package pt.ipleiria.estg.dei.amsi.mobilesportwine.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Review;

public class ReviewJsonParser {
    public static ArrayList<Review> parserJsonReviews(JSONArray response) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);

                int id = obj.getInt("id");
                int userId = obj.getInt("user_id");
                int productId = obj.getInt("product_id");
                int rating = obj.getInt("rating");
                String comment = obj.getString("comment");
                String createdAt = obj.getString("created_at");

                reviews.add(new Review(id, userId, productId, rating, comment, createdAt));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}