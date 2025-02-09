package pt.ipleiria.estg.dei.amsi.mobilesportwine;
import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.CarrinhoAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CheckoutListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhosListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class CarrinhoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, CarrinhoListener, CheckoutListener {
    private RecyclerView rvCarrinho;
    private ArrayList<Vinho> listaVinhos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnFinalizarCompra;

    public CarrinhoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        rvCarrinho = view.findViewById(R.id.recyclerCarrinho);
        rvCarrinho.setLayoutManager(new LinearLayoutManager(getContext()));


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        btnFinalizarCompra = view.findViewById(R.id.btnFinalizarCompra);
        btnFinalizarCompra.setOnClickListener(v -> finalizarCompra());

        SingletonManager.getInstance(getContext()).setCheckoutListener(this);

        SingletonManager.getInstance(getContext()).setCarrinhoListener(this);
        SingletonManager.getInstance(getContext()).getCartAPI(getContext());

        return view;
    }

    @Override
    public void onFinalizarCompraSucesso(int invoiceId, double totalPrice) {
        // Passar os dados para o CheckoutFragment
        Bundle bundle = new Bundle();
        bundle.putInt("invoice_id", invoiceId);
        bundle.putDouble("total_price", totalPrice);



        CheckoutFragment checkoutFragment = new CheckoutFragment();
        checkoutFragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFragment, checkoutFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onErroFinalizarCompra(String erro) {
        Snackbar.make(getView(), erro, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPagamentoSucesso(int invoiceId, int orderId, double totalAmount, String status) {
        // Passar para a tela de confirmação do pedido
        System.out.println("--> invoiceCarrinhoFRAGMENT:" + invoiceId); // DEBUG AQUI

    }

    @Override
    public void onErroPagamento(String erro) {
        Snackbar.make(getView(), erro, Snackbar.LENGTH_SHORT).show();
    }


    private void finalizarCompra() {
        ArrayList<ItemCarrinho> itensCarrinho = SingletonManager.getInstance(getContext()).getCarrinhoItems();
        SingletonManager.getInstance(getContext()).finalizarCompra(getContext(), itensCarrinho, new CheckoutListener() {
            @Override
            public void onFinalizarCompraSucesso(int invoiceId, double totalPrice) {
                // Passar os dados para o CheckoutFragment
                Bundle bundle = new Bundle();
                bundle.putInt("invoice_id", invoiceId);
                bundle.putDouble("total_price", totalPrice);

                CheckoutFragment checkoutFragment = new CheckoutFragment();
                checkoutFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contentFragment, checkoutFragment);
                transaction.addToBackStack(null); // Adicionar a transação na pilha
                transaction.commit();
            }

            @Override
            public void onErroFinalizarCompra(String erro) {
                // Exibir erro
                Snackbar.make(getView(), erro, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onPagamentoSucesso(int invoiceId, int orderId, double totalAmount, String status) {
                // Exibir erro
                Snackbar.make(getView(), "Pagamento efetuado", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onErroPagamento(String erro) {
                // Exibir erro
                Snackbar.make(getView(), erro, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRefresh() {
        SingletonManager.getInstance(getContext()).getCartAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefreshListaCarrinho(ArrayList<ItemCarrinho> listaItens) {
        if (getView() == null) {
            System.out.println("--> ❌ ERRO: getView() é null, adiando atualização.");
            return;
        }

        listaVinhos = SingletonManager.getInstance(getContext()).getVinhos();

        if (listaItens == null || listaItens.isEmpty()) {
            System.out.println("--> ❌ ERRO: Nenhum item no carrinho!");
            return;
        }

        if (listaVinhos == null || listaVinhos.isEmpty()) {
            System.out.println("--> ❌ ERRO: Nenhuma informação dos vinhos!");
            return;
        }

        // Atualizar RecyclerView
        rvCarrinho.setAdapter(new CarrinhoAdaptador(getContext(), listaItens, listaVinhos));

        // Só chama atualizarBotaoCheckout() se a View estiver pronta
        atualizarBotaoCheckout();
    }


    private void atualizarBotaoCheckout() {
        if (getView() == null) {
            System.out.println("--> ❌ ERRO: getView() retornou null, não pode atualizar botão.");
            return;
        }

        double total = SingletonManager.getInstance(getContext()).getTotalCarrinho();
        Button btnFinalizarCompra = getView().findViewById(R.id.btnFinalizarCompra);

        if (btnFinalizarCompra != null) {
            String textoBotao = String.format(Locale.getDefault(), "Finalizar Compra - €%.2f", total);
            btnFinalizarCompra.setText(textoBotao);
        } else {
            System.out.println("--> ❌ ERRO: Botão Finalizar Compra não encontrado na View.");
        }
    }




    @Override
    public void onCarrinhoAlterado() {
        Snackbar.make(getView(), "Livro Removido com Sucesso", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onErroRemover(String erro) {

    }


}
