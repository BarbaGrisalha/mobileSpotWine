package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class ListaVinhosAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Vinho> vinhos;

    public ListaVinhosAdaptador(Context context, ArrayList<Vinho> vinhos) {
        this.context = context;
        this.vinhos = vinhos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return vinhos.size();
    }

    @Override
    public Object getItem(int position) {
        return vinhos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return vinhos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLista viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista_vinho, parent, false);
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderLista) convertView.getTag();
        }

        viewHolder.update(vinhos.get(position));
        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvName, tvDescription, tvCategory, tvPrice, tvStock;
        private ImageView imgCapa;
        private ImageButton btnAddToCart, btnFavorite;
        private EditText etQuantidade;

        public ViewHolderLista(View view) {
            tvName = view.findViewById(R.id.tvName);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvStock = view.findViewById(R.id.tvStock);
            imgCapa = view.findViewById(R.id.imgCapaVinho);
            btnAddToCart = view.findViewById(R.id.btnAddToCart);
            btnFavorite = view.findViewById(R.id.btnFavorite);
            etQuantidade = view.findViewById(R.id.etQuantidade);
        }

        public void update(final Vinho vinho) {
            tvName.setText(vinho.getName());
            tvDescription.setText(vinho.getDescription());
            tvCategory.setText(vinho.getCategory());
            tvStock.setText(vinho.getStock() + " unidades");

            // Formatar preço para exibição em moeda (€ por padrão)
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            tvPrice.setText(format.format(vinho.getPrice()));

            // Carregar imagem com Glide (com melhor cache e fallback)
            Glide.with(context)
                    .load(vinho.getImage())
                    .placeholder(R.drawable.wine_default) // Imagem de espera
//                    .error(R.drawable.wine_error) // Se a imagem falhar
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);

            // Configurar clique no botão "Adicionar ao Carrinho"
            btnAddToCart.setOnClickListener(v -> {
                if (!isValidToCart(vinho)) return; // Sai caso não seja válido

                int quantidade = Integer.parseInt(etQuantidade.getText().toString().trim());

                // Criar objeto ItemCarrinho e chamar API
                ItemCarrinho item = new ItemCarrinho(vinho.getId(), quantidade);
                SingletonManager.getInstance(context).addItemCarrinhoAPI(item, context);

                System.out.println("--> item:" + item);

                Toast.makeText(context, "Vinho adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
            });

            if (SingletonManager.getInstance(context).isFavorito(vinho.getId())) {
                btnFavorite.setImageResource(R.drawable.ic_menu_favorited);
            } else {
                btnFavorite.setImageResource(R.drawable.ic_menu_unfavorited);
            }

            btnFavorite.setOnClickListener(v -> {
                if (SingletonManager.getInstance(context).isFavorito(vinho.getId())) {
                    SingletonManager.getInstance(context).removeFavorito(vinho.getId());
                    btnFavorite.setImageResource(R.drawable.ic_menu_unfavorited);
                } else {
                    SingletonManager.getInstance(context).addFavorito(vinho.getId());
                    btnFavorite.setImageResource(R.drawable.ic_menu_favorited);
                }
            });

        }

        //TODO: TALVEZ FAZER UM METODO PUBLICO EM OUTRO LUGAR PARA OUTRAS PARTES USAREM TBM
        private boolean isValidToCart(Vinho vinho) {
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
    }
}
