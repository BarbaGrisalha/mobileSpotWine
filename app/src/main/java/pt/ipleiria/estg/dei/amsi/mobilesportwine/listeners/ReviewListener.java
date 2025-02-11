package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Post;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Review;

public interface ReviewListener {
    void onRefreshReviews(ArrayList<Review> reviews, int id);
}
