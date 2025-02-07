package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class ListaVinhosAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Vinho> vinhos;

    public ListaVinhosAdaptador(Context context, ArrayList<Vinho> vinhos) {
        this.context = context;
        this.vinhos = vinhos;
    }

    @Override
    public int getCount() {
        return vinhos.size();
    }

    @Override
    public Object getItem(int i) {
        return vinhos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return vinhos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.item_lista_vinho, null);
        }

        ViewHolderLista viewHolderLista = (ViewHolderLista) view.getTag();

        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        }

        viewHolderLista.update(vinhos.get(i));

        return view;
    }

    private class ViewHolderLista{
        private TextView tvName, tvDescription, tvCategory, tvPrice, tvStock;
        private ImageView imgCapa;

        public ViewHolderLista(View view){
            tvName = view.findViewById(R.id.tvName);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvCategory = view.findViewById(R.id.tvCategory); // Novo campo
            tvPrice = view.findViewById(R.id.tvPrice);
            tvStock = view.findViewById(R.id.tvStock); // Novo campo
            imgCapa = view.findViewById(R.id.imgCapa);
        }

        public void update(Vinho vinho){
            tvName.setText(vinho.getName());
            tvDescription.setText(vinho.getDescription());
            tvCategory.setText(vinho.getCategory()); // Mostra a categoria
            tvStock.setText(vinho.getStock() + " unidades"); // Mostra o stock

            // Formatar preço para exibição em moeda (€ por padrão)
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            tvPrice.setText(format.format(vinho.getPrice()));

            // Carregar imagem com Glide
            Glide.with(context)
                    .load(vinho.getImage())
                    .placeholder(R.drawable.wine_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }
}
