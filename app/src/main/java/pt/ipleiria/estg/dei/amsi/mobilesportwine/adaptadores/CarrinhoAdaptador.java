package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class CarrinhoAdaptador extends RecyclerView.Adapter<CarrinhoAdaptador.ViewHolder> {

    private Context context;
    private ArrayList<ItemCarrinho> carrinho;
    private ArrayList<Vinho> listaVinhos;


    public CarrinhoAdaptador(Context context, ArrayList<ItemCarrinho> carrinho, ArrayList<Vinho> listaVinhos) {
        this.context = context;
        this.carrinho = carrinho;
        this.listaVinhos = listaVinhos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.update(carrinho.get(position), position);
    }

    @Override
    public int getItemCount() {
        return carrinho.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNome, tvPreco;
        private ImageView imgVinho;
        private EditText etQuantidade;
        private ImageButton btnRemover;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeVinho);
            tvPreco = itemView.findViewById(R.id.tvPrecoVinho);
            imgVinho = itemView.findViewById(R.id.imgVinho);
            etQuantidade = itemView.findViewById(R.id.etQuantidade);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }

        public void update(ItemCarrinho item, int position) {
            tvNome.setText(item.getProductName());
            tvPreco.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(item.getPrice()));
            etQuantidade.setText(String.valueOf(item.getQuantity()));

            String imageUrl = getImagemDoVinho(item.getProductId());
            if (imageUrl != null) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.wine_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVinho);
            }

            btnRemover.setOnClickListener(v -> {
                System.out.println("--> 🗑️ Removendo item: " + item.getProductName());
                removerItem(position);
            });

        }
    }

    private String getImagemDoVinho(int productId) {
        System.out.println("--> Procurando imagem para o produto ID: " + productId);

        for (Vinho vinho : listaVinhos) {
            System.out.println("Vinho: " + vinho.getId() + " - " + vinho.getImage());
            if (vinho.getId() == productId) {
                return vinho.getImage();
            }
        }
        System.out.println("--> Nenhuma imagem encontrada para o produto ID: " + productId);
        return null;
    }

    public void removerItem(int position) {
        ItemCarrinho item = carrinho.get(position);
        int itemId = item.getId();

        SingletonManager.getInstance(context).removerItemCarrinhoAPI(context, itemId, new CarrinhoListener() {
            @Override
            public void onRefreshListaCarrinho(ArrayList<ItemCarrinho> listaItens) {
                System.out.println("--> 🔄 Atualizando RecyclerView após remoção...");
                System.out.println("--> Lista de itens recebida: " + listaItens.size());

                // Atualiza a lista local
                carrinho.clear();
                carrinho.addAll(listaItens);

                // Notifica a RecyclerView
                notifyDataSetChanged();
            }

            @Override
            public void onCarrinhoAlterado() {
                System.out.println("--> ✅ Item removido, atualizando UI...");
            }

            @Override
            public void onErroRemover(String erro) {
                Toast.makeText(context, "Erro ao remover: " + erro, Toast.LENGTH_SHORT).show();
            }
        });
    }




}
