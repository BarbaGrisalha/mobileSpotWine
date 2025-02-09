package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Post;

public class PostAdaptador extends RecyclerView.Adapter<PostAdaptador.PostViewHolder> {

    private Context context;
    private ArrayList<Post> posts;
    private OnItemClickListener onItemClickListener;

    // Interface para o clique nos itens
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Construtor com o Listener
    public PostAdaptador(Context context, ArrayList<Post> posts, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.posts = posts;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.txtTitle.setText(post.getTitle());
        holder.txtContent.setText(post.getContent());
        holder.txtDate.setText(post.getCreatedAt());

        // Configura o clique nos itens
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));

        // Carrega a imagem com Glide
        Glide.with(context)
                .load("http://51.20.254.239:8080" + post.getImageUrl())
                .into(holder.imgPost);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // ViewHolder para o Post
    public class PostViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtContent, txtDate;
        ImageView imgPost;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgPost = itemView.findViewById(R.id.imgPost);
        }
    }
}
