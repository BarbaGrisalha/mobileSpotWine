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

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class FavoritosAdaptador extends RecyclerView.Adapter<FavoritosAdaptador.ViewHolder> {
    private Context context;
    private ArrayList<Vinho> listaVinhos;

    public FavoritosAdaptador(Context context, ArrayList<Vinho> listaVinhos) {
        this.context = context;
        this.listaVinhos = listaVinhos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_vinho, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listaVinhos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaVinhos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice;
        private ImageView imgCapa;
        private ImageButton btnRemoveFavorito, btnAddToCart;
        private EditText etQuantidade;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgCapa = itemView.findViewById(R.id.imgCapaVinho);
            btnRemoveFavorito = itemView.findViewById(R.id.btnFavorite);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            etQuantidade = itemView.findViewById(R.id.etQuantidade);

        }

        public void bind(final Vinho vinho) {
            tvName.setText(vinho.getName());
            tvPrice.setText(String.format("€ %.2f", vinho.getPrice()));


            Glide.with(context).load(vinho.getImage()).into(imgCapa);

            btnRemoveFavorito.setOnClickListener(v -> {
                SingletonManager.getInstance(context).removeFavorito(vinho.getId());
                listaVinhos.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });

            btnAddToCart.setOnClickListener(v -> {
                if (!isValidToCart(vinho)) return;

                int quantidade = Integer.parseInt(etQuantidade.getText().toString().trim());

                ItemCarrinho item = new ItemCarrinho(vinho.getId(), quantidade);
                SingletonManager.getInstance(context).addItemCarrinhoAPI(item, context);

                Toast.makeText(context, "Vinho adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
            });



        }

        private boolean isValidToCart(Vinho vinho) {
            String quantidadeStr = etQuantidade.getText().toString().trim();

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

    }
}
