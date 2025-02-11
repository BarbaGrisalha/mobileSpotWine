package pt.ipleiria.estg.dei.amsi.mobilesportwine.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.InvoiceAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.InvoiceListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Invoice;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

public class FaturasFragment extends Fragment implements InvoiceListener {

    private RecyclerView rvInvoices;
    private InvoiceAdaptador adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faturas, container, false);

        rvInvoices = view.findViewById(R.id.rvInvoices);
        rvInvoices.setLayoutManager(new LinearLayoutManager(getContext()));

        // Buscar as faturas
        SingletonManager.getInstance(getContext()).getInvoicesAPI(getContext(), this);

        return view;
    }

    @Override
    public void onRefreshInvoices(ArrayList<Invoice> invoices) {
        if (invoices != null && !invoices.isEmpty()) {
            adapter = new InvoiceAdaptador(invoices, getContext());
            rvInvoices.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "Nenhuma fatura encontrada.", Toast.LENGTH_SHORT).show();
        }
    }
}