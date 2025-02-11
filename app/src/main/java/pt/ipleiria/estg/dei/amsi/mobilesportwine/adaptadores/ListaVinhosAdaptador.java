package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class ListaVinhosAdaptador extends RecyclerView.Adapter<ListaVinhosAdaptador.VinhoViewHolder> {

    private Context context;
    private ArrayList<Vinho> vinhos;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Construtor
    public ListaVinhosAdaptador(Context context, ArrayList<Vinho> vinhos, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.vinhos = vinhos;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_lista_vinho, parent, false);
        return new VinhoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VinhoViewHolder holder, int position) {
        Vinho vinho = vinhos.get(position);

        holder.tvName.setText(vinho.getName());
        holder.tvCategory.setText("Categoria: " + vinho.getCategory());
        holder.tvPrice.setText("€" + vinho.getPrice());
        holder.tvStock.setText("Em estoque: " + vinho.getStock() + " unidades");

        // Carregar imagem com Glide
        Glide.with(context)
                .load(vinho.getImage())
                .into(holder.imgCapaVinho);

        // Configura o clique no item
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return vinhos.size();
    }

    // ViewHolder
    public static class VinhoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice, tvStock;
        ImageView imgCapaVinho;

        public VinhoViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            imgCapaVinho = itemView.findViewById(R.id.imgCapaVinho);
        }
    }
}
