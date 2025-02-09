package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.MenuMainActivity;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.CheckoutListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.LoginListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.PostListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.PostsListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhosListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.CarrinhoJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.ConnectivityJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.LoginJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.PostJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.VinhoJsonParser;

public class SingletonManager {
    private static SingletonManager instance = null;

    private ArrayList<Vinho> vinhos;
    private ArrayList<ItemCarrinho> itensCarrinho;
    private ArrayList<Post> posts;


    private VinhoBDHelper  vinhoBDHelper = null;

    private static RequestQueue volleyQueue = null;

    private static final String mUrlAPIVinhos = "http://51.20.254.239:8080/api/product";
    private static final String mUrlAPILogin = "http://51.20.254.239:8080/api/user/login";
    private static final String mUlAPICart = "http://51.20.254.239:8080/api/cart";
    private static final String URL_API_POSTS = "http://51.20.254.239:8080/api/blog-post";

    private VinhosListener vinhosListener;
    private VinhoListener vinhoListener;
    private LoginListener loginListener;
    private CarrinhoListener carrinhoListener;
    private CheckoutListener checkoutListener;
    private PostsListener postsListener;
    private PostListener postListener;

    public static synchronized SingletonManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonManager(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonManager(Context context) {
        //gerarDadosDinamico();
        vinhos = new ArrayList<>();
        vinhoBDHelper = new VinhoBDHelper(context);

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
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIVinhos,
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

    //TODO: FALTA COLOCAR O PARAMETRO DE TOKEN
    public void adicionarVinhoAPI(final Vinho vinho, final Context context) {
        if (!ConnectivityJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
            return;
        }

        // Recuperar o token do SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("AUTH_DATA", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(context, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIVinhos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Adicionar vinho no BD local e atualizar o listener
                        adicionarVinhoBD(VinhoJsonParser.parserJsonVinho(response));

                        if (vinhoListener != null) {
                            vinhoListener.onRefreshDetalhes(MenuMainActivity.ADD);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token); // Usar o token recuperado do SharedPreferences
                params.put("name", vinho.getName());
                params.put("description", vinho.getDescription());
                params.put("price", String.valueOf(vinho.getPrice()));
//                params.put("capa", vinho.getCapa());
                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void editarVinhoAPI(final Vinho vinho, final Context context){
        if(!ConnectivityJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
        }else {
            StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPIVinhos + '/' + vinho.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    editarVinhoBD(vinho);

                    //TODO: IMPLEMENTAR OS LISTENERS
                    if(vinhoListener != null){
                        vinhoListener.onRefreshDetalhes(MenuMainActivity.EDIT);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String,String>();
                    params.put("token", "AMSI-TOKEN");
                    params.put("name", vinho.getName());
                    params.put("description", vinho.getDescription());
                    params.put("price", vinho.getPrice() + "");
                    params.put("capa", vinho.getImage());
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void removerVinhoAPI(final Vinho vinho, final Context context){
        if(!ConnectivityJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show();
        }else {
            StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIVinhos + '/' + vinho.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    removerVinhoBD(vinho.getId());

                    //TODO: IMPLEMENTAR LISTENERS
                    if(vinhoListener != null){
                        vinhoListener.onRefreshDetalhes(MenuMainActivity.DELETE);
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUlAPICart + "?access-token=" + token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("--> Resposta da API: " + response.toString());

                        Carrinho carrinho = CarrinhoJsonParser.parseCarrinho(response);

                        if (carrinho != null) {
                            System.out.println("--> Carrinho: " + carrinho.toString());

                            // Atualiza a lista de itens do carrinho
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

        StringRequest request = new StringRequest(Request.Method.POST,
                mUlAPICart + "/items?access-token=" + token,
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

        StringRequest request = new StringRequest(Request.Method.DELETE,
                mUlAPICart + "/items/" + itemId + "?access-token=" + token,

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

        String url = mUlAPICart + "?access-token=" + token; // Endpoint para limpar o carrinho

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
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
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_API_POSTS, null,
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

        String url = "http://51.20.254.239:8080/api/blog-post?access-token=gib1WP8VjzEZ1EfvLlEqWDbezvzqVfzY";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Post novoPost = PostJsonParser.parserJsonPost(response);
//                        adicionarPostBD(newPost); // Adiciona ao BD local

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
        System.out.println("--> id do post: "+post.getId());
        String url = "http://51.20.254.239:8080/api/blog-post/" + post.getId() + "?access-token=gib1WP8VjzEZ1EfvLlEqWDbezvzqVfzY";

        StringRequest request = new StringRequest(Request.Method.PUT, url,
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

        String url = "http://51.20.254.239:8080/api/blog-post/" + post.getId() + "?access-token=gib1WP8VjzEZ1EfvLlEqWDbezvzqVfzY";

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
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


    public void loginAPI(String email, String password, final Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String token = String.valueOf(LoginJsonParser.parseLoginData(response)); // Parse do token
                System.out.println("---- token " + token);

                if (loginListener != null)
                    loginListener.onValidateLogin(token, email, context); // Callback do listener
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginAPI", "Erro no login: ", error);

                String errorMessage;
                if (error instanceof TimeoutError) {
                    errorMessage = "Tempo de resposta expirou. Tente novamente.";
                } else if (error instanceof NoConnectionError) {
                    errorMessage = "Sem conexão com a internet. Verifique sua rede.";
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "Falha na autenticação. Verifique suas credenciais.";
                } else {
                    errorMessage = "Erro inesperado: " + error.getMessage();
                }

                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
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


    //endregion


}
