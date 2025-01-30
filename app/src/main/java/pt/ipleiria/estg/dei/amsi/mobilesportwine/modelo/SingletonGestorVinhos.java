package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.amsi.mobilesportwine.MenuMainActivity;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.R;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.LoginListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhoListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners.VinhosListener;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.LoginJsonParser;
import pt.ipleiria.estg.dei.amsi.mobilesportwine.utils.VinhoJsonParser;

public class SingletonGestorVinhos {

    private ArrayList<Vinho> vinhos;

    private static SingletonGestorVinhos instance = null;

    private VinhoBDHelper  vinhoBDHelper = null;

    private static RequestQueue volleyQueue = null;

    private static final String mUrlAPIVinhos = "http://51.20.254.239:8080/api/product";
    private static final String mUrlAPILogin = "http://51.20.254.239:8080/api/user/login";

    private VinhosListener vinhosListener;
    private VinhoListener vinhoListener;
    private LoginListener loginListener;

    public static synchronized SingletonGestorVinhos getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorVinhos(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonGestorVinhos(Context context) {
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
        if(!VinhoJsonParser.isConnectionInternet(context)){
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
        if (!VinhoJsonParser.isConnectionInternet(context)) {
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
        if(!VinhoJsonParser.isConnectionInternet(context)){
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
                    params.put("capa", vinho.getCapa());
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void removerVinhoAPI(final Vinho vinho, final Context context){
        if(!VinhoJsonParser.isConnectionInternet(context)){
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
