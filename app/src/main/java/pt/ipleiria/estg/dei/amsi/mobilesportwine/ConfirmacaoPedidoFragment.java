package pt.ipleiria.estg.dei.amsi.mobilesportwine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.ItemCarrinho;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

public class ConfirmacaoPedidoFragment extends Fragment {

    public ConfirmacaoPedidoFragment() {
        // Construtor vazio obrigatório
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmacao_pedido, container, false);

        TextView txtPedidoId = view.findViewById(R.id.txtPedidoId);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        TextView txtTotal = view.findViewById(R.id.txtTotal);
        Button btnContinuar = view.findViewById(R.id.btnContinuar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int invoiceId = bundle.getInt("invoice_id", -1);
            int orderId = bundle.getInt("order_id", -1);
            double totalPrice = bundle.getDouble("total_price", 0.0);
            String status = bundle.getString("status", "N/A");

            txtPedidoId.setText("Pedido ID: " + orderId);
            txtStatus.setText("Status: " + status);
            txtTotal.setText(String.format(Locale.getDefault(), "Total: €%.2f", totalPrice));
        } else {
            Toast.makeText(getContext(), "Nenhum dado recebido", Toast.LENGTH_SHORT).show();
        }

        // Configurar o clique do botão "Continuar Comprando" para limpar o carrinho e ir para ListaVinhosFragment
        btnContinuar.setOnClickListener(v -> {
            SingletonManager.getInstance(getContext()).limparCarrinho(getContext(), new CarrinhoListener() {
                @Override
                public void onRefreshListaCarrinho(ArrayList<ItemCarrinho> listaItens) {
                    // Implementação opcional
                }

                @Override
                public void onCarrinhoAlterado() {
                    // Após limpar o carrinho, navega para ListaVinhosFragment
                    ListaVinhosFragment listaFragment = new ListaVinhosFragment();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentFragment, listaFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                @Override
                public void onErroRemover(String erro) {
                    Snackbar.make(requireView(), "Erro ao limpar o carrinho: " + erro, Snackbar.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}
