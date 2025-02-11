package pt.ipleiria.estg.dei.amsi.mobilesportwine;
import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.DetalhesVinhoActivity;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.ListaVinhosAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhosListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class ListaVinhosFragment extends Fragment implements VinhosListener {

    private RecyclerView rvVinhos;
    private ArrayList<Vinho> vinhoList;
    private ListaVinhosAdaptador vinhoAdaptador;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_vinhos, container, false);
        rvVinhos = view.findViewById(R.id.lvVinhos);
        rvVinhos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa a lista de vinhos
        vinhoList = new ArrayList<>();
        vinhoAdaptador = new ListaVinhosAdaptador(getContext(), vinhoList, position -> {
            Intent intent = new Intent(getContext(), DetalhesVinhoActivity.class);
            Vinho vinho = vinhoList.get(position);
            intent.putExtra("vinhoId", vinho.getId());
            startActivity(intent);
        });

        rvVinhos.setAdapter(vinhoAdaptador);

        SingletonManager.getInstance(getContext()).setVinhosListener(this);

        // Buscar vinhos da API (ou mockados por enquanto)
        SingletonManager.getInstance(getContext()).getAllVinhosAPI(getContext());

        return view;
    }

    public void onRefreshListaVinhos(ArrayList<Vinho> vinhos) {
        vinhoList.clear();
        vinhoList.addAll(vinhos);
        vinhoAdaptador.notifyDataSetChanged();
    }
}
