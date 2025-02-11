package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.LoginActivity;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.MenuMainActivity;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.adaptadores.ReviewAdaptador;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CheckoutListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.InvoiceListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.LoginListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.PostListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.PostsListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.ReviewListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhosListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.CarrinhoJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.ConnectivityJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.InvoiceJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.LoginJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.PostJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.ReviewJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.VinhoJsonParser;

public class SingletonManager {
    private static SingletonManager instance = null;

    private ArrayList<Vinho> vinhos;
    private ArrayList<ItemCarrinho> itensCarrinho;
    private ArrayList<Post> posts;
    private ArrayList<Review> reviews;
    private Context context;
    private Set<Integer> favoritos;

    private static final String API_HOSTNAME_KEY = "API_HOSTNAME";
    private String apiHostname;


    private VinhoBDHelper  vinhoBDHelper = null;

    private static RequestQueue volleyQueue = null;

//    private static final String mUrlAPIVinhos = "http://51.20.254.239:8080/api/product";
//    private static final String mUrlAPILogin = "http://51.20.254.239:8080/api/user/login";
//    private static final String mUrlAPIRegister = "http://51.20.254.239:8080/api/user/registo";
//    private static final String mUlAPICart = "http://51.20.254.239:8080/api/cart";
//    private static final String URL_API_POSTS = "http://51.20.254.239:8080/api/blog-post";

    private VinhosListener vinhosListener;
    private VinhoListener vinhoListener;
    private LoginListener loginListener;
    private CarrinhoListener carrinhoListener;
    private CheckoutListener checkoutListener;
    private PostsListener postsListener;
    private PostListener postListener;
    private ReviewListener reviewListener;
    private InvoiceListener invoiceListener;


    public static synchronized SingletonManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonManager(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private String getApiHostname() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(API_HOSTNAME_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(API_HOSTNAME_KEY, null); // Default se não existir
    }

    // 🔹 Métodos para obter URLs dinâmicas
    public String getUrlApiVinhos() {
        return apiHostname + "/api/product";
    }

    public String getUrlApiLogin() {
        return apiHostname + "/api/user/login";
    }

    public String getUrlApiRegister() {
        return apiHostname + "/api/user/registo";
    }

    public String getUrlApiCart() {
        return apiHostname + "/api/cart";
    }

    public String getUrlApiPosts() {
        return apiHostname + "/api/blog-post";
    }

    public String getUrlApiFavorites() {
        return apiHostname + "/api/favorite";
    }

    public String getUrlApiReview() {
        return apiHostname + "/api/review";
    }

    private SingletonManager(Context context) {
        //gerarDadosDinamico();
        vinhos = new ArrayList<>();
        vinhoBDHelper = new VinhoBDHelper(context);
        this.favoritos = new HashSet<>();
        this.context = context;
        this.apiHostname = getApiHostname();

    }

    //REGISTO DOS LISTENERS
    public void setVinhosListener(VinhosListener vinhosListener) {
        this.vinhosListener = vinhosListener;
    }


    public void setVinhoListener(VinhoListener vinhoListener) {
        this.vinhoListener = vinhoListener;
    }

    public void setLoginListener(LoginListener loginListener){
        this.loginListener = loginListener;
    }
    public void setCarrinhoListener(CarrinhoListener carrinhoListener){
        this.carrinhoListener = carrinhoListener;
    }
    public void setCheckoutListener(CheckoutListener checkoutListener){
        this.checkoutListener = checkoutListener;
    }

    public void setPostsListener(PostsListener  listener) {
        this.postsListener = listener;
    }

    public void setPostListener(PostListener listener) {
        this.postListener = listener;
    }

    public void setReviewsListener(ReviewListener listener) {
        this.reviewListener = listener;
    }

    public void setInvoiceListener(InvoiceListener listener) {
        this.invoiceListener = listener;
    }


    public ArrayList<Vinho> getVinhosBD() {
        vinhos = vinhoBDHelper.getAllVinhosBD();
        return new ArrayList<>(vinhos);
    }

    // Recebe um id como parametro para no Detalhes vinhos Activity receber o livro específico
    public Vinho getVinho(int id){
        for (Vinho l: vinhos) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    public ArrayList<Vinho> getVinhos(){
        return vinhos;
    }

    public ArrayList<ItemCarrinho> getCarrinhoItems() {
        return itensCarrinho;
    }

    public Post getPost(int id){
        for (Post p: posts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }


    public void adicionarVinhoBD(Vinho vinho){
        vinhoBDHelper.adicionarVinhoBD(vinho);
    }

    public void editarVinhoBD(Vinho vinho){
        vinhoBDHelper.editarVinhoBD(vinho);


    }

    public void removerVinhoBD(int idVinho){
        vinhoBDHelper.removerVinhoBD(idVinho);
    }

    public void adicionarVinhosBD(ArrayList<Vinho> vinhos){
        vinhoBDHelper.removerAllVinhosBD();
        for(Vinho l : vinhos){
            adicionarVinhoBD(l);
        }
    }


    //region #MÉTODOS - API #
    public void getAllVinhosAPI(final Context context){
        if(!ConnectivityJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();

            //TODO:CARREGAR vinhos DA BD ATRAVÉS DO METHOD getAllvinhosBD
            if(vinhosListener != null){
                vinhosListener.onRefreshListaVinhos(vinhoBDHelper.getAllVinhosBD());
            }

        }else{
            String url = SingletonManager.getInstance(context).getUrlApiVinhos();
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println("-->GETAPI: " + response);

                    vinhos = VinhoJsonParser.parserJsonVinhos(response);
                    adicionarVinhosBD(vinhos);

                    //TODO: IMPLEMENTAR LISTENERS
                    if(vinhosListener != null){
                        vinhosListener.onRefreshListaVinhos(vinhos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            volleyQueue.add(request);


        }
    }


    public void getCartAPI(Context context) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SingletonManager.getInstance(context).getUrlApiCart();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + "?access-token=" + token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Carrinho carrinho = CarrinhoJsonParser.parseCarrinho(response);

                        if (carrinho != null) {
                            itensCarrinho = carrinho.getItems();

                            if (itensCarrinho != null && !itensCarrinho.isEmpty()) {
                                System.out.println("--> Itens do Carrinho:" + itensCarrinho);
                                for (ItemCarrinho item : itensCarrinho) {
                                    System.out.println(item.toString());
                                }
                            } else {
                                System.out.println("--> Itens do Carrinho estão vazios ou nulos!");
                            }

                            // Notifica o adaptador para atualizar a lista
                            if (carrinhoListener != null) {
                                carrinhoListener.onRefreshListaCarrinho(itensCarrinho);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("Erro ao obter carrinho: " + error.getMessage());
                    }
                }
        );

        volleyQueue.add(request);
    }

    public void addItemCarrinhoAPI(final ItemCarrinho item, final Context context) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SingletonManager.getInstance(context).getUrlApiCart();

        StringRequest request = new StringRequest(Request.Method.POST,
                url + "/items?access-token=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("-->POSTAPI: " + response);
                        Toast.makeText(context, "Vinho adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                        getCartAPI(context); // Atualiza a lista do carrinho
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao adicionar vinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(item.getProductId()));
                params.put("quantity", String.valueOf(item.getQuantity()));
                System.out.println("--> info Parametros api "+ item.getProductId() +" e " +  item.getQuantity());
                return params;
            }

        };

        volleyQueue.add(request);
    }


    public void removerItemCarrinhoAPI(Context context, int itemId, CarrinhoListener listener) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        String url = SingletonManager.getInstance(context).getUrlApiCart();

        StringRequest request = new StringRequest(Request.Method.DELETE,
                url + "/items/" + itemId + "?access-token=" + token,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("--> Id produto"+ itemId);
                        System.out.println("--> ✅ Item removido com sucesso!");
                        getCartAPI(context); // Atualiza a lista do carrinho
                        if (listener != null) {
                            listener.onCarrinhoAlterado();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("--> ❌ Erro ao remover item: " + error.getMessage());
                        if (listener != null) {
                            listener.onErroRemover(error.getMessage());
                        }
                    }
                });

        volleyQueue.add(request);
    }

    public void limparCarrinho(final Context context, final CarrinhoListener listener) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SingletonManager.getInstance(context).getUrlApiPosts();

        StringRequest request = new StringRequest(Request.Method.DELETE, url + "?access-token=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("--> Carrinho limpo: " + response);
                        if (listener != null) {
                            listener.onCarrinhoAlterado();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao limpar o carrinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onErroRemover(error.getMessage());
                        }
                    }
                }
        );
        volleyQueue.add(request);
    }



    public double getTotalCarrinho() {
        double total = 0.0;
        for (ItemCarrinho item : itensCarrinho) {
            total += item.getPrice() * item.getQuantity(); // Multiplica preço pela quantidade
        }
        return total;
    }

    public void finalizarCompra(final Context context, ArrayList<ItemCarrinho> itensCarrinho, final CheckoutListener listener) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);
        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria o JSONArray de itens
        JSONArray items = new JSONArray();
        for (ItemCarrinho item : itensCarrinho) {
            try {
                JSONObject itemJson = new JSONObject();
                itemJson.put("product_id", item.getProductId());
                System.out.println("--> produto: " + item.getProductId());
                itemJson.put("quantity", item.getQuantity());
                System.out.println("--> quantidade: " + item.getQuantity());
                items.put(itemJson);
                System.out.println("--> item json: " + itemJson);


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Erro ao criar o JSON dos itens", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Cria o objeto JSON do corpo da requisição
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("items", items);
            System.out.println("--> items: " + items);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao criar o corpo da requisição", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://51.20.254.239:8080/api/checkout/create?access-token=" + token;

        // Cria a requisição usando JsonObjectRequest, que lida com JSONObject
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obter dados da resposta
                            JSONObject order = response.getJSONObject("order");
                            System.out.println("--> order " + order);
                            JSONObject invoice = response.getJSONObject("invoice");
                            System.out.println("--> invoice " + invoice);
                            int invoiceId = invoice.getInt("id");
                            System.out.println("--> invoiceId " + invoiceId);
                            double totalPrice = invoice.getDouble("total_amount");
                            System.out.println("--> totalPrice " + totalPrice);

                            System.out.println("--> invoiceID RETORNADO DA API: " + invoiceId);
                            // Chama o método de sucesso do listener
                            listener.onFinalizarCompraSucesso(invoiceId, totalPrice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onErroFinalizarCompra("Erro ao processar a resposta");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErroFinalizarCompra("Erro ao criar o pedido: " + error.getMessage());
                    }
                });

        volleyQueue.add(request);
    }




    public void pagarPedido(final Context context, int invoiceId, final CheckoutListener listener) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL da API de pagamento
        String url = "http://51.20.254.239:8080/api/checkout/" + invoiceId + "?access-token=" + token;
        System.out.println("--> invoiceiDentifi " + invoiceId);

        // Requisição POST
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String message = responseObj.getString("message");

                            if (message.equals("Pagamento realizado.")) {
                                // Extraia os dados da fatura
                                JSONObject invoice = responseObj.getJSONObject("invoice");
                                int newInvoiceId = invoice.getInt("id"); // ID da fatura
                                int orderId = invoice.getInt("order_id"); // ID do pedido
                                double totalAmount = invoice.getDouble("total_amount");
                                String status = invoice.getString("status");

                                // Notifica o listener com os dados completos
                                listener.onPagamentoSucesso(newInvoiceId, orderId, totalAmount, status);
                            } else {
                                listener.onErroPagamento("Erro ao realizar o pagamento");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onErroPagamento("Erro ao processar o pagamento");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao realizar pagamento: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        volleyQueue.add(request);
    }

    public void getAllPostsAPI(final Context context) {
        // Obtém a URL dinâmica do SingletonManager
        String url = SingletonManager.getInstance(context).getUrlApiPosts();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts = PostJsonParser.parserJsonPosts(response);
                        if (postsListener != null) {
                            postsListener.onRefreshListaPosts(posts);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao buscar posts: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        volleyQueue.add(request);
    }


    public void adicionarPostAPI(final Post post, final Context context) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);


        String url = SingletonManager.getInstance(context).getUrlApiPosts();

        StringRequest request = new StringRequest(Request.Method.POST, url + "?access-token="+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (postListener != null) {
                            postListener.onRefreshDetalhesPost(MenuMainActivity.ADD);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao adicionar post: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", post.getTitle());
                params.put("content", post.getContent());
                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void editarPostAPI(final Post post, final Context context) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        String url = SingletonManager.getInstance(context).getUrlApiPosts();

        StringRequest request = new StringRequest(Request.Method.PUT, url+"/"+post.getId() + "?access-token="+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        editarPostBD(post);

                        if (postListener != null) {
                            postListener.onRefreshDetalhesPost(MenuMainActivity.EDIT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao editar post: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", post.getTitle());
                params.put("content", post.getContent());
                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void deletarPostAPI(final Post post, final Context context) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        String url = SingletonManager.getInstance(context).getUrlApiPosts();

        StringRequest request = new StringRequest(Request.Method.DELETE, url+"/"+ post.getId() + "?access-token="+token,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        removerPostBD(post.getId());

                        if (postListener != null) {
                            postListener.onRefreshDetalhesPost(MenuMainActivity.DELETE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao deletar post: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        volleyQueue.add(request);
    }

    // Método para adicionar um produto aos favoritos
    public void addFavorito(int produtoId) {
        if (!favoritos.contains(produtoId)) {
            favoritos.add(produtoId); // Adiciona se não estiver já favoritado

            addFavoritoAPI(produtoId);
            Toast.makeText(context, "Adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
        }
    }


        // Método para remover um produto dos favoritos
        public void removeFavorito(int produtoId) {
            if (favoritos.contains(produtoId)) {
                favoritos.remove(produtoId); // Remove da lista
                // Chama a API para remover no backend
                removeFavoritoAPI(produtoId);
                Toast.makeText(context, "Removido dos favoritos!", Toast.LENGTH_SHORT).show();
            }
        }

        // Método para verificar se o produto está nos favoritos
        public boolean isFavorito(int produtoId) {
            return favoritos.contains(produtoId);
        }

        // Adiciona favorito no backend (API)
        private void addFavoritoAPI(int produtoId) {
            if (!ConnectivityJsonParser.isConnectionInternet(context)) {
                Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("TOKEN", null);

            if (token == null) {
                Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = SingletonManager.getInstance(context).getUrlApiFavorites();

            StringRequest request = new StringRequest(Request.Method.POST, url+"/"+produtoId+
                    "?access-token="+token, response -> {
                // Sucesso ao adicionar aos favoritos
               addFavorito(produtoId);
               getFavoritosAPI(context);

            }, error -> {
                // Trate erros aqui
                Log.e("Favoritos", "Erro ao adicionar aos favoritos: " + error.getMessage());
            });
            volleyQueue.add(request);
        }

        // Remove favorito no backend (API)
        private void removeFavoritoAPI(int produtoId) {
            if (!ConnectivityJsonParser.isConnectionInternet(context)) {
                Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("TOKEN", null);

            if (token == null) {
                Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = SingletonManager.getInstance(context).getUrlApiFavorites();
            StringRequest request = new StringRequest(Request.Method.DELETE, url+"/"+produtoId+"?access-token="+token, response -> {
                // Sucesso ao remover dos favoritos
               removeFavorito(produtoId);
               getFavoritosAPI(context);
            }, error -> {
                // Trate erros aqui
                Log.e("Favoritos", "Erro ao remover dos favoritos: " + error.getMessage());
            });

            volleyQueue.add(request);
        }

    public void getFavoritosAPI(Context context) {

        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SingletonManager.getInstance(context).getUrlApiFavorites();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"?access-token="+ token, null,
                response -> {
                    try {
                        JSONArray favoritosArray = response.getJSONArray("favorite_ids");
                        favoritos.clear();
                        for (int i = 0; i < favoritosArray.length(); i++) {
                            favoritos.add(favoritosArray.getInt(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Erro ao buscar favoritos: " + error.getMessage());
                }
        );

        volleyQueue.add(request);
    }


        public Set<Integer> getFavoritos() {
            return favoritos;
        }

    public void getReviewsAPI(int productId, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        String url = SingletonManager.getInstance(context).getUrlApiReview();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url+"/"+productId, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Faz o parsing do JSON para a lista de reviews
                        reviews = ReviewJsonParser.parserJsonReviews(response);

                        if (reviewListener != null) {
                            reviewListener.onRefreshReviews(reviews, productId); // Passa o productId como segundo parâmetro
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao buscar reviews: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adiciona a requisição à fila do Volley
        volleyQueue.add(request);
    }

    public void adicionarReviewAPI(final Review review, int productId, final Context context) {
        // Verifica a conexão com a internet
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SingletonManager.getInstance(context).getUrlApiReview();

        // Cria a requisição POST
        StringRequest request = new StringRequest(Request.Method.POST, url+"?access-token="+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        getReviewsAPI(productId, context);

                        Toast.makeText(context, "Review adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Depuração: Imprime o erro
                        System.err.println("Erro ao adicionar review: " + error.getMessage());

                        Toast.makeText(context, "Erro ao adicionar review: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                // Parâmetros do corpo da requisição
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId)); // ID do produto
                params.put("rating", String.valueOf(review.getRating())); // Nota da review
                params.put("comment", review.getComment()); // Comentário da review
                return params;
            }
        };

        // Adiciona a requisição à fila do Volley
        volleyQueue.add(request);
    }

    public void editarReviewAPI(final Review review, final Context context) {
        // Verifica a conexão com a internet
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SingletonManager.getInstance(context).getUrlApiReview();

        StringRequest request = new StringRequest(Request.Method.PUT, url+"/"+review.getId() + "?access-token="+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Depuração: Imprime a resposta da API
                        System.out.println("--> Resposta da API (Editar Review): " + response);

                        getReviewsAPI(review.getProductId(), context);

                        Toast.makeText(context, "Review editada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Depuração: Imprime o erro
                        System.err.println("Erro ao editar review: " + error.getMessage());

                        Toast.makeText(context, "Erro ao editar review: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                // Parâmetros do corpo da requisição
                Map<String, String> params = new HashMap<>();
                params.put("rating", String.valueOf(review.getRating())); // Nova nota da review
                params.put("comment", review.getComment()); // Novo comentário da review
                return params;
            }
        };

        // Adiciona a requisição à fila do Volley
        volleyQueue.add(request);
    }

    public void deletarReviewAPI(final int reviewId, final int productId, final Context context) {
        // Verifica a conexão com a internet
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SingletonManager.getInstance(context).getUrlApiReview();

        // Cria a requisição DELETE
        StringRequest request = new StringRequest(Request.Method.DELETE, url+"/"+reviewId+"?access-token=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Depuração: Imprime a resposta da API
                        System.out.println("--> Resposta da API (Deletar Review): " + response);

                        getReviewsAPI(productId, context);

                        Toast.makeText(context, "Review deletada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Depuração: Imprime o erro
                        System.err.println("Erro ao deletar review: " + error.getMessage());

                        Toast.makeText(context, "Erro ao deletar review: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adiciona a requisição à fila do Volley
        volleyQueue.add(request);
    }

    public void getInvoicesAPI(Context context, InvoiceListener listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://51.20.254.239:8080/api/invoice/my-invoices?access-token=" + token;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Invoice> invoices = InvoiceJsonParser.parserJsonInvoices(response);
                        if (listener != null) {
                            listener.onRefreshInvoices(invoices);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao buscar faturas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adiciona a requisição à fila do Volley
        volleyQueue.add(request);
    }


    public void loginAPI(String email, String password, final Context context) {
        String url = SingletonManager.getInstance(context).getUrlApiLogin();
        System.out.println("--> url do login: "+url);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String token = LoginJsonParser.parseLoginToken(response); // Pega o token
                        int userId = LoginJsonParser.parseUserId(response); // Pega o ID do usuário

                        if (loginListener != null)
                            loginListener.onValidateLogin(token, userId, email, context);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LoginAPI", "Erro no login: ", error);
                        Toast.makeText(context, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);
                return params;
            }
        };
        volleyQueue.add(request);
    }

    public void registerAPI(String username, String email, String password, String nif, String phone, final Context context) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("nif", nif);
            jsonBody.put("phone_number", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = SingletonManager.getInstance(context).getUrlApiRegister();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Snackbar.make(((Activity) context).findViewById(android.R.id.content),
                            "Conta criada com sucesso!", Snackbar.LENGTH_LONG).show();
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }, 1500);
                },
                error -> {
                    Log.e("RegisterAPI", "Erro ao registrar: ", error);
                    Toast.makeText(context, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }





    //endregion


}
