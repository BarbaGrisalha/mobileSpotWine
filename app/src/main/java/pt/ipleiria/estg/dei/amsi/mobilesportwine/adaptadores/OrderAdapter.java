package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.invoiceNumber.setText("Fatura #"+order.getInvoiceNumber());
        holder.invoiceDate.setText("Data: "+order.getInvoiceDate());
        holder.totalAmount.setText("Total: "+String.valueOf(order.getTotalAmount()));
        holder.status.setText("Status: "+order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView invoiceNumber, invoiceDate, totalAmount, status;

        public OrderViewHolder(View itemView) {
            super(itemView);
            invoiceNumber = itemView.findViewById(R.id.tvInvoiceNumber);
            invoiceDate = itemView.findViewById(R.id.tvInvoiceDate);
            totalAmount = itemView.findViewById(R.id.tvTotalAmount);
            status = itemView.findViewById(R.id.tvStatus);
        }
    }
}