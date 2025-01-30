package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public interface VinhosListener {
    void onRefreshListaVinhos(ArrayList<Vinho> listaVinhos);
}
