package br.com.blbbb.blbb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.blbbb.blbb.config.Config;
import br.com.blbbb.blbb.dbHelper.ConexaoSQLite;
import br.com.blbbb.blbb.model.ItemDoPedido;
import br.com.blbbb.blbb.model.Produto;
import br.com.blbbb.blbb.notificacao.BLNotificacao;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;

    //para conexao ao webservice
    private RequestQueue mRequestQueue;

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "EXECUTANDO TAREFA");

        this.context = context;

        final String url = Config.getUrlBase(ConexaoSQLite.getInstancia(context)) + "post/postPedido.php?action=listarItensProntos";

        this.getItensDoPedidoProntosWebService(url, this.context, this.mRequestQueue);

        Log.d(TAG, url);

    }

    /**
     * Recupero os itens de um pedido no servidor
     *
     * @param pUrl
     */
    private void getItensDoPedidoProntosWebService(String pUrl, final Context context, RequestQueue mRequestQueue) {

        //requisicao usando o volley do google
        mRequestQueue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                List<ItemDoPedido> listaItensPedido = new ArrayList<>();

//                Log.d(TAG, response.toString());

                String itensProntos = "";

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray arrayItemDoPedido = jsonObject.getJSONArray("itens_do_pedido");
                    ItemDoPedido itemDoPedido = null;
                    JSONObject itemDoPedidoJson = null;
                    Produto produtoDoPedido = null;

                    int countPedidos = arrayItemDoPedido.length();

                    if (countPedidos > 0) {

                        for (int i = 0; i < countPedidos; i++) {
                            itemDoPedidoJson = arrayItemDoPedido.getJSONObject(i);
                            itemDoPedido = new ItemDoPedido();

                            itemDoPedido.setCodigo(itemDoPedidoJson.getLong("cod_ped"));
                            itemDoPedido.setMesaCodigo(itemDoPedidoJson.getLong("cod_mes"));
                            itemDoPedido.setObservacao(itemDoPedidoJson.getString("obs"));
                            itemDoPedido.setStatus(itemDoPedidoJson.getString("status_ped"));
                            itemDoPedido.setQuantidade(itemDoPedidoJson.getInt("quantidade_iten"));

                            produtoDoPedido = new Produto();
                            produtoDoPedido.setCodigo(itemDoPedidoJson.getLong("cod_prod"));
                            produtoDoPedido.setNome(itemDoPedidoJson.getString("nome_prod"));

                            itemDoPedido.setProduto(produtoDoPedido);

                            itensProntos += "Mesa: " + itemDoPedido.getMesaCodigo() + " - " + produtoDoPedido.getNome() + " \n ";

//                                Log.d(TAG, itemDoPedido.getProduto().getNome());

                            listaItensPedido.add(itemDoPedido);
                        }

                        BLNotificacao blNotificacao = new BLNotificacao(context);
                        blNotificacao.criarNotificacao(context, "Itens prontos!", itensProntos);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error: " + e.getMessage());
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                System.out.println("Response is: " + error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                long garcomCodigo = Config.getConfigGarcomCodigo(ConexaoSQLite.getInstancia(context));

                //seto os dados que irao para o servidor
                Map<String, String> params = new HashMap<String, String>();
//                params.put("postQuantidadeItens", String.valueOf(pListItensDoCardapio.size()));
//                params.put("postMesaNumero", String.valueOf(mesaDoPedido.getMeCodigo()));
//                params.put("postItensPedido", itensDoPedidoJson);
                params.put("postGarcomCodigo", String.valueOf(garcomCodigo));

                return params;
            }
        };

        // Set the tag on the request.
        stringRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
}
