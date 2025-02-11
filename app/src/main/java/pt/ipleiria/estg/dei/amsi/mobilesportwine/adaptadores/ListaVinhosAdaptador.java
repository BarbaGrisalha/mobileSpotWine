package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

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

        // Atualizar os dados do vinho
        holder.tvName.setText(vinho.getName());
        holder.tvCategory.setText("Categoria: " + vinho.getCategory());
        holder.tvPrice.setText("€" + vinho.getPrice());
        holder.tvStock.setText("Em estoque: " + vinho.getStock() + " unidades");

        // Carregar imagem com Glide
        Glide.with(context)
                .load(vinho.getImage())
                .into(holder.imgCapaVinho);

        // Configurar o clique no item
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));

        // Configurar o botão de adicionar ao carrinho
        holder.btnAddToCart.setOnClickListener(v -> {
            if (!isValidToCart(vinho, holder.etQuantidade)) return; // Valida a quantidade

            int quantidade = Integer.parseInt(holder.etQuantidade.getText().toString().trim());

            // Criar objeto ItemCarrinho e chamar API
            ItemCarrinho item = new ItemCarrinho(vinho.getId(), quantidade);
            SingletonManager.getInstance(context).addItemCarrinhoAPI(item, context);

            Toast.makeText(context, "Vinho adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
        });

        if (SingletonManager.getInstance(context).isFavorito(vinho.getId())) {
            holder.btnFavorite.setImageResource(R.drawable.ic_menu_favorited);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_menu_unfavorited);
        }

        holder.btnFavorite.setOnClickListener(v -> {
            if (SingletonManager.getInstance(context).isFavorito(vinho.getId())) {
                SingletonManager.getInstance(context).removeFavorito(vinho.getId());
                holder.btnFavorite.setImageResource(R.drawable.ic_menu_unfavorited);
            } else {
                SingletonManager.getInstance(context).addFavorito(vinho.getId());
                holder.btnFavorite.setImageResource(R.drawable.ic_menu_favorited);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vinhos.size();
    }



    // Função para validar a quantidade antes de adicionar ao carrinho
    private boolean isValidToCart(Vinho vinho, EditText etQuantidade) {
        String quantidadeStr = etQuantidade.getText().toString().trim();

        // Verifica se a quantidade foi inserida
        if (quantidadeStr.isEmpty()) {
            Toast.makeText(context, "Insira uma quantidade!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Digite um número válido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se a quantidade é válida
        if (quantidade <= 0) {
            Toast.makeText(context, "Quantidade deve ser maior que zero!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (quantidade > vinho.getStock()) {
            Toast.makeText(context, "Estoque insuficiente!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // ViewHolder
    public static class VinhoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice, tvStock;
        ImageView imgCapaVinho;
        EditText etQuantidade;
        ImageButton btnAddToCart, btnFavorite;

        public VinhoViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            imgCapaVinho = itemView.findViewById(R.id.imgCapaVinho);
            etQuantidade = itemView.findViewById(R.id.etQuantidade);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}