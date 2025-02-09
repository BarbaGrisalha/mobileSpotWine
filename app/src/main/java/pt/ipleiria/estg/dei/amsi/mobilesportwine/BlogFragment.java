package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.PostAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.PostsListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Post;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

public class BlogFragment extends Fragment implements PostsListener {

    private RecyclerView rvPosts;
    private FloatingActionButton fabListaPost;
    private ArrayList<Post> postList;
    private PostAdaptador postAdaptador;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa lista vazia para evitar NullPointerException
        postList = new ArrayList<>();
        postAdaptador = new PostAdaptador(getContext(), postList, new PostAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), DetalhesPostActivity.class);
                Post post = postList.get(position);
                intent.putExtra("postId", post.getId());
                startActivityForResult(intent, MenuMainActivity.EDIT);
            }
        });

        fabListaPost = view.findViewById(R.id.fabListaPost);
        fabListaPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "teste", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), DetalhesPostActivity.class);
                //startActivity(intent);
                //START ACTIVITY FOR RESULT
                startActivityForResult(intent, MenuMainActivity.ADD);
            }
        });

        rvPosts.setAdapter(postAdaptador);

        // Configura o listener para ser notificado quando os posts chegarem
        SingletonManager.getInstance(getContext()).setPostsListener(this);

        // Chama a API para buscar os posts
        SingletonManager.getInstance(getContext()).getAllPostsAPI(getContext());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == MenuMainActivity.ADD || requestCode == MenuMainActivity.EDIT){

                SingletonManager.getInstance(getContext()).getAllPostsAPI(getContext());

                switch(requestCode){
                    case MenuMainActivity.ADD:
                        Snackbar.make(getView(), "Post Adicionado com Sucesso", Snackbar.LENGTH_SHORT).show();
                        break;

                    case MenuMainActivity.EDIT:
                        if(data.getIntExtra(MenuMainActivity.OP_CODE,0) == MenuMainActivity.DELETE){
                            Snackbar.make(getView(), "Post Removido com Sucesso", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(getView(), "Post Editado com Sucesso", Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    default:
                        break;
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRefreshListaPosts(ArrayList<Post> posts) {
        // Atualiza a lista de posts quando a API responder
        postList.clear();
        postList.addAll(posts);
        postAdaptador.notifyDataSetChanged();
    }
}
