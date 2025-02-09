package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;

public interface CarrinhoListener {
    void onRefreshListaCarrinho(ArrayList<ItemCarrinho> listaItens);
    void onCarrinhoAlterado();
    void onErroRemover(String erro);
}

