package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class Review {
    private int id;
    private int userId;
    private int productId;
    private int rating;
    private String comment;
    private String createdAt;

    public Review(int id, int userId, int productId, int rating, String comment, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    // Métodos get
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getCreatedAt() { return createdAt; }

    // Métodos set
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}