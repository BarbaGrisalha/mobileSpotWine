package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.CheckoutAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CheckoutListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Vinho;

public class CheckoutFragment extends Fragment {
    private RecyclerView rvCarrinhoCheckout;
    private TextView txtTotal;
    private Button btnPagar;
    private ArrayList<ItemCarrinho> carrinhoItems;
    private ArrayList<Vinho> listaVinhos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        rvCarrinhoCheckout = view.findViewById(R.id.recyclerCarrinhoCheckout);
        rvCarrinhoCheckout.setLayoutManager(new LinearLayoutManager(getContext()));

        txtTotal = view.findViewById(R.id.txtTotal);
        btnPagar = view.findViewById(R.id.btnPagar);
        System.out.println("--> Bundle recebido: " + getArguments());

//        // Receber os dados do Bundle
//        int invoiceId = getArguments().getInt("invoice_id");
//        double totalPrice = getArguments().getDouble("total_price");
//        System.out.println("--> invoiceCHECKOUTFRAGMENT: " + invoiceId);

        Bundle bundle = getArguments();
        int invoiceId = bundle.getInt("invoice_id", -1);
        double totalPrice = bundle.getDouble("total_price", 0.0);





        txtTotal.setText(String.format(Locale.getDefault(), "Total: €%.2f", totalPrice));

        // Obter os itens do carrinho e a lista de vinhos
        carrinhoItems = SingletonManager.getInstance(getContext()).getCarrinhoItems();
        listaVinhos = SingletonManager.getInstance(getContext()).getVinhos();

        // Configurar o CheckoutAdapter se houver itens
        if (carrinhoItems != null && !carrinhoItems.isEmpty() && listaVinhos != null && !listaVinhos.isEmpty()) {
            CheckoutAdaptador adapter = new CheckoutAdaptador(getContext(), carrinhoItems, listaVinhos);
            rvCarrinhoCheckout.setAdapter(adapter);
        } else {
            Snackbar.make(getView(), "Nenhum item no carrinho", Snackbar.LENGTH_SHORT).show();
        }

        // Configurar o botão Pagar
        btnPagar.setOnClickListener(v -> {
            // Suponha que você já tenha recuperado invoiceId do Bundle

            SingletonManager.getInstance(getContext()).pagarPedido(getContext(), invoiceId, new CheckoutListener() {
                @Override
                public void onPagamentoSucesso(int invoiceId, int orderId, double totalAmount, String status) {
                    // Exibir os dados no log para depuração
                    System.out.println("--> invoiceCheckoutFRAGMENT: " + invoiceId);
                    System.out.println("--> orderId: " + orderId);
                    System.out.println("--> totalAmount: " + totalAmount);
                    System.out.println("--> status: " + status);

                    // Navegar para o ConfirmacaoPedidoFragment com os dados
                    ConfirmacaoPedidoFragment confirmacaoFragment = new ConfirmacaoPedidoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("invoice_id", invoiceId);
                    bundle.putInt("order_id", orderId);
                    bundle.putDouble("total_price", totalAmount);
                    bundle.putString("status", status);
                    confirmacaoFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentFragment, confirmacaoFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                @Override
                public void onErroPagamento(String erro) {
                    Snackbar.make(getView(), erro, Snackbar.LENGTH_SHORT).show();
                }

                // Se você estiver usando também onFinalizarCompraSucesso, pode implementá-lo ou deixar vazio
                @Override
                public void onFinalizarCompraSucesso(int invoiceId, double totalPrice) {
                    // Não utilizado aqui

                }

                @Override
                public void onErroFinalizarCompra(String erro) {
                    // Não utilizado aqui
                }
            });
        });


        return view;
    }
}
