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

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class ListaVinhosAdaptador extends BaseAdapter {
    private Context context;  // Contexto da aplicação ou da Activity para acessar recursos do sistema
    private LayoutInflater inflater;  // Inflater para criar views a partir de arquivos XML
    private ArrayList<Vinho> vinhos;  // Lista de objetos Livro a serem exibidos

    // Construtor do adaptador que recebe o contexto e a lista de livros
    public ListaVinhosAdaptador(Context context, ArrayList<Vinho> vinhos) {
        this.context = context;  // Armazena o contexto
        this.vinhos = vinhos;    // Armazena a lista de livros
    }

    // Retorna a quantidade de itens na lista
    @Override
    public int getCount() {
        return vinhos.size();
    }

    // Retorna o objeto Livro na posição 'i'
    @Override
    public Object getItem(int i) {
        return vinhos.get(i);
    }

    // Retorna o ID do item na posição 'i' (baseado no ID do Livro)
    @Override
    public long getItemId(int i) {
        return vinhos.get(i).getId();
    }

    // Método principal que cria e retorna a view para cada item da lista
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Inicializa o inflater se ainda não foi inicializado
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // Se a view ainda não foi criada, infla o layout do item da lista
        if(view == null){
            view = inflater.inflate(R.layout.item_lista_vinho, null);
        }

        // Tenta obter o ViewHolder associado à view atual (usando a tag da view)
        ViewHolderLista viewHolderLista = (ViewHolderLista) view.getTag();

        // Se o ViewHolder ainda não existe (primeira vez que esta view é usada), cria um novo
        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);  // Associa o ViewHolder à view usando setTag
        }

        // Atualiza o ViewHolder com os dados do livro atual
        viewHolderLista.update(vinhos.get(i));

        // Retorna a view para exibir na lista
        return view;
    }

    // Classe interna ViewHolder para armazenar as referências dos elementos da view
    private class ViewHolderLista{
        private TextView tvName, tvDescription, tvPrice;  // Elementos de texto para mostrar informações do livro
        private ImageView imgCapa;  // Elemento de imagem para mostrar a capa do livro

        // Construtor do ViewHolder que inicializa as referências dos elementos da view
        public ViewHolderLista(View view){
            tvName = view.findViewById(R.id.tvName);  // Referência para o título do livro
            tvDescription= view.findViewById(R.id.tvDescription);    // Referência para a série do livro
            tvPrice = view.findViewById(R.id.tvPrice);        // Referência para o ano do livro
            // Referência para o autor do livro
            imgCapa = view.findViewById(R.id.imgCapa);    // Referência para a imagem da capa do livro
        }

        // Método para atualizar os dados da view com as informações do livro
        public void update(Vinho vinho){
            tvName.setText(vinho.getName());  // Define o título do livro
            tvDescription.setText(vinho.getDescription());    // Define a série do livro
            tvPrice.setText(""+vinho.getPrice());    // Define o autor do livro

            Glide.with(context)
                    .load(vinho.getCapa())
                    .placeholder(R.drawable.wine_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);  // Define a imagem da capa do livro
        }
    }
}
