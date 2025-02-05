package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Order;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    public OrderAdapter(List<Order> orderList) {
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Pedito #"+order.getId());
        holder.tvOrderDate.setText("Data: "+order.getDate());
        holder.tvOrderTotal.setText("Total: €"+order.getTotal());
    }

    @Override
    public int getItemCount(){
        return orderList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvOrderId, tvOrderDate, tvOrderTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}
