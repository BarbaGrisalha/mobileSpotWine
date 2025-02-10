package pt.ipleiria.estg.dei.amsi.mobilesportwine;
import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.FavoritosAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class FavoritosFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoritosAdaptador favoritosAdaptador;
    private ArrayList<Vinho> listaVinhosFavoritos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        recyclerView = view.findViewById(R.id.rvFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carregarFavoritos();

        return view;
    }

    private void carregarFavoritos() {
        // Pegando a lista de IDs dos favoritos
        Set<Integer> favoritos = SingletonManager.getInstance(getContext()).getFavoritos();

        // Filtrando apenas os vinhos que estão nos favoritos
        listaVinhosFavoritos.clear();
        for (Vinho vinho : SingletonManager.getInstance(getContext()).getVinhos()) {
            if (favoritos.contains(vinho.getId())) {
                listaVinhosFavoritos.add(vinho);
            }
        }

        // Atualizando a lista no adaptador
        favoritosAdaptador = new FavoritosAdaptador(getContext(), listaVinhosFavoritos);
        recyclerView.setAdapter(favoritosAdaptador);
    }
}
