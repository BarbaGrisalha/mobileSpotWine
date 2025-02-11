package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Invoice;

public class InvoiceAdaptador extends RecyclerView.Adapter<InvoiceAdaptador.ViewHolder> {
    private ArrayList<Invoice> invoices;
    private Context context;

    public InvoiceAdaptador(ArrayList<Invoice> invoices, Context context) {
        this.invoices = invoices;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvoiceNumber, tvInvoiceDate, tvTotalAmount, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvInvoiceNumber = itemView.findViewById(R.id.tvInvoiceNumber);
            tvInvoiceDate = itemView.findViewById(R.id.tvInvoiceDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        holder.tvInvoiceNumber.setText(invoice.getInvoiceNumber());
        holder.tvInvoiceDate.setText(invoice.getInvoiceDate());
        holder.tvTotalAmount.setText(invoice.getTotalAmount());
        holder.tvStatus.setText(invoice.getStatus());
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }
}