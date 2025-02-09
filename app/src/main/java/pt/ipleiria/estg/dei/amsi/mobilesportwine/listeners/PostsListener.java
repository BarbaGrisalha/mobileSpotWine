package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Post;

public interface PostsListener {
    void onRefreshListaPosts(ArrayList<Post> posts);
}
