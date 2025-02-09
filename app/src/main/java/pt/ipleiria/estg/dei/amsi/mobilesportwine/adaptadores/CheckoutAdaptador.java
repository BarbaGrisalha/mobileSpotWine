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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class CheckoutAdaptador extends RecyclerView.Adapter<CheckoutAdaptador.ViewHolder> {
    private Context context;
    private ArrayList<ItemCarrinho> carrinhoItems;
    private ArrayList<Vinho> listaVinhos;

    public CheckoutAdaptador(Context context, ArrayList<ItemCarrinho> carrinhoItems, ArrayList<Vinho> listaVinhos) {
        this.context = context;
        this.carrinhoItems = carrinhoItems;
        this.listaVinhos = listaVinhos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(carrinhoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return carrinhoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNome, tvQuantidade, tvPreco;
        private ImageView imgVinho;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeVinho);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            tvPreco = itemView.findViewById(R.id.tvPrecoVinho);
            imgVinho = itemView.findViewById(R.id.imgVinho);
        }

        public void bind(ItemCarrinho item) {
            tvNome.setText(item.getProductName());
            tvQuantidade.setText("Quantidade: " + item.getQuantity());
            tvPreco.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(item.getPrice()));

            String imageUrl = getImagemDoVinho(item.getProductId());
            if (imageUrl != null) {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.wine_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgVinho);
            }
        }
    }

    private String getImagemDoVinho(int productId) {
        for (Vinho vinho : listaVinhos) {
            if (vinho.getId() == productId) {
                return vinho.getImage();
            }
        }
        return null;
    }
}
