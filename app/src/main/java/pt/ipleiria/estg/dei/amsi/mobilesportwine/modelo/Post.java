package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

public class Post {
    private int id;
    private String title, content, imageUrl, createdAt;

    public Post(int id, String title, String content, String imageUrl, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public String getCreatedAt() { return createdAt; }

    //Métodos Setters
    public void setTitle(String titulo) {
        this.title = titulo;
    }

    public void setContent(String conteudo) {
        this.content = conteudo;
    }


}
