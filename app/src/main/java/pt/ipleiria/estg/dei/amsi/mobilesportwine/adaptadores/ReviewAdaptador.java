package pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.Review;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo.SingletonManager;

public class ReviewAdaptador extends RecyclerView.Adapter<ReviewAdaptador.ViewHolder> {
    private List<Review> reviews;
    private int reviewId;
    private int idUser;
    private Context context; // Contexto para acessar o SingletonManager

    public ReviewAdaptador(List<Review> reviews, int reviewId, Context context) {
        this.reviews = reviews;
        this.reviewId = reviewId;
        this.context = context;

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        this.idUser = sharedPreferences.getInt("USER_ID", -1);

        if (reviews != null && !reviews.isEmpty()) {
            System.out.println("--> Lista de Reviews (Após Parsing):");
            for (Review review : reviews) {
                System.out.println("-->cada review" + review.toString()); // Imprime cada Review
            }
        } else {
            System.out.println("--> Lista de Reviews está vazia ou nula!");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvComment, tvRating, tvCreatedAt;
        ImageView btnDelete, btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvComment.setText(review.getComment());
        holder.tvRating.setText("⭐ " + review.getRating() + "/5");
        holder.tvCreatedAt.setText(review.getCreatedAt());

        // Verificar se o usuário logado é o autor da review
        if (review.getUserId() == idUser) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);

            // Listener para o botão de deletar
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        SingletonManager.getInstance(context).deletarReviewAPI(
                                review.getId(), review.getProductId(), context
                        );
                        reviews.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, reviews.size());
                        Toast.makeText(context, "Review deletada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Listener para o botão de editar
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        // Exibir o dialog de edição
                        exibirDialogEdicao(review, adapterPosition);
                    }
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private void exibirDialogEdicao(Review review, int position) {
        // Inflar o layout do dialog
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editar_review, null);

        // Referências dos componentes do dialog
        EditText etEditarComentario = dialogView.findViewById(R.id.etEditarComentario);
        RatingBar ratingBarEditar = dialogView.findViewById(R.id.ratingBarEditar);
        Button btnSalvarEdicao = dialogView.findViewById(R.id.btnSalvarEdicao);

        // Preencher os campos com os dados atuais da review
        etEditarComentario.setText(review.getComment());
        ratingBarEditar.setRating(review.getRating());

        // Criar o dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Listener para o botão "Salvar"
        btnSalvarEdicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recuperar os dados editados
                String novoComentario = etEditarComentario.getText().toString().trim();
                int novaClassificacao = (int) ratingBarEditar.getRating();

                // Validar os campos
                if (novoComentario.isEmpty()) {
                    etEditarComentario.setError("Por favor, insira um comentário.");
                    return;
                }

                if (novaClassificacao < 1 || novaClassificacao > 5) {
                    Toast.makeText(context, "A classificação deve ser entre 1 e 5 estrelas.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Atualizar a review com os novos dados
                review.setComment(novoComentario);
                review.setRating(novaClassificacao);

                SingletonManager.getInstance(context).editarReviewAPI(review, context);

                notifyItemChanged(position);
                Toast.makeText(context, "Review editada com sucesso!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }
}