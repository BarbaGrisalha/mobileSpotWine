package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.ListaVinhosAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhosListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonGestorVinhos;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class ListaVinhosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, VinhosListener {

    private ListView lvVinhos;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ListaVinhosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_vinhos, container, false);

        setHasOptionsMenu(true);

        lvVinhos = view.findViewById(R.id.lvVinhos);

//        lvVinhos.setOnClickListener(
//             new AdapterView.OnItemClickListener(){
//                 @Override
//                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent intent = new Intent(getContext(), DetalhesVinhoActivity.class);
//
//                    intent.putExtra(DetalhesVinhoActivity.ID_VINHO, (int) l);
//
//                    startActivityForResult(intent, MenuMainActivity.EDIT);
//                 }
//             }
//        );


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        SingletonGestorVinhos.getInstance(getContext()).setVinhosListener(this);
        SingletonGestorVinhos.getInstance(getContext()).getAllVinhosAPI(getContext());

        return view;
    }


    public void onRefresh(){
        SingletonGestorVinhos.getInstance(getContext()).getAllVinhosAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshListaVinhos(ArrayList<Vinho> listaVinhos) {
        if(listaVinhos != null)
            lvVinhos.setAdapter(new ListaVinhosAdaptador(getContext(),listaVinhos));
    }
}