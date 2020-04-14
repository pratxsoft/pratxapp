package br.com.blbbb.blbb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

import br.com.blbbb.blbb.R;
import br.com.blbbb.blbb.adapter.list.ItensDoPedidoAdapter;
import br.com.blbbb.blbb.adapter.spinner.MesaSpinnerAdapter;
import br.com.blbbb.blbb.config.Config;
import br.com.blbbb.blbb.dbHelper.ConexaoSQLite;
import br.com.blbbb.blbb.model.ItemDoPedido;
import br.com.blbbb.blbb.model.Mesa;
import br.com.blbbb.blbb.model.Produto;

public class BarActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //gui componentes
    private ListView lvItensDoPedido;
    private TextView tvBarMesa;
    private Spinner spMainMesa; //mesas cadastradas
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //objetos
    private List<Mesa> listaMesa;
    private List<ItemDoPedido> listaItensDoPedido;

    //adapters
    private MesaSpinnerAdapter mesaSpinnerAdapter;
    private ItensDoPedidoAdapter itensDoPedidoAdapter;

    //intents
    private Intent intentCardapio;

    //para conexao ao webservice
    private RequestQueue mRequestQueue;
    private static final String TAG = "queue_wbs_bar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        this.iniciarObjetos();
        this.iniciarIntents();
        this.listarMesas();
        this.initEvents();
    }

    /**
     * Busco a lista de mesas no servidor e preencho o spinner mesas
     */
    private void listarMesas() {

        //requisicao usando o volley do google
        this.mRequestQueue = Volley.newRequestQueue(this);
        final String url = Config.getUrlBase(ConexaoSQLite.getInstancia(BarActivity.this)) + "post/postPedido.php?action=listarMesas";

        getMesasLocal();
    }

    private void getMesasLocal() {
        List<Mesa> listaMesas = new ArrayList<>();
        Mesa mesa = null;

        for (int i = 1; i < 51; i++) {
            mesa = new Mesa();
            mesa.setMeCodigo(i);
            mesa.setMeMesa(String.valueOf(i));
            mesa.setMeSituacao("");
            listaMesas.add(mesa);
        }

        //preencho o spinner com as mesas vindas do servidor
        popularSpinnerMesa(listaMesas);
    }


     /**
     * Busco os itens do pedido de uma mesa
     */
    private void litarItensDoPedido() {

        //recupeo a mesa selecionada no spiner
        final Mesa mesaSelecionada = (Mesa) spMainMesa.getSelectedItem();


        //requisicao usando o volley do google
        this.mRequestQueue = Volley.newRequestQueue(this);
        final String url = Config.getUrlBase(ConexaoSQLite.getInstancia(BarActivity.this)) + "post/postPedido.php?action=listarItensPedido";

        getItensDoPedidoWebService(url, mesaSelecionada);

    }

    /**
     * Recupero os itens de um pedido no servidor
     *
     * @param pUrl
     */
    private void getItensDoPedidoWebService(String pUrl, final Mesa pMesaSelecionada) {

        //requisicao usando o volley do google
        this.mRequestQueue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        List<ItemDoPedido> listaItensPedido = new ArrayList<>();

                        Log.d("ITENS PEDI ", response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray arrayItemDoPedido = jsonObject.getJSONArray("itens_do_pedido");
                            ItemDoPedido itemDoPedido = null;
                            JSONObject itemDoPedidoJson = null;
                            Produto produtoDoPedido = null;

                            for (int i = 0; i < arrayItemDoPedido.length(); i++) {
                                itemDoPedidoJson = arrayItemDoPedido.getJSONObject(i);
                                itemDoPedido = new ItemDoPedido();

                                itemDoPedido.setCodigo(itemDoPedidoJson.getLong("cod_ped"));
                                itemDoPedido.setMesaCodigo(itemDoPedidoJson.getLong("cod_mes"));
                                itemDoPedido.setObservacao(itemDoPedidoJson.getString("obs"));
                                itemDoPedido.setStatus(itemDoPedidoJson.getString("status_ped"));
                                itemDoPedido.setQuantidade(Float.parseFloat(itemDoPedidoJson.getString("quantidade_iten")));

                                produtoDoPedido = new Produto();
                                produtoDoPedido.setCodigo(itemDoPedidoJson.getLong("cod_prod"));
                                produtoDoPedido.setNome(itemDoPedidoJson.getString("nome_prod"));

                                itemDoPedido.setProduto(produtoDoPedido);

                                Log.d(TAG, itemDoPedido.getProduto().getNome());

                                listaItensPedido.add(itemDoPedido);
                            }

                            //preencho o listview com as mesas vindas do servidor
                            popularListViewItensPedido(listaItensPedido);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error_: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BarActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Response is: " + error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                //seto os dados que irao para o servidor
                Map<String, String> params = new HashMap<String, String>();
                params.put("postMesaNumero", pMesaSelecionada.getMeMesa());

                return params;
            }
        };

        // Set the tag on the request.
        stringRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

    /**
     * popula o spinner de mesas
     */
    private void popularSpinnerMesa(List<Mesa> pListaMesa) {
        //seto a lista de mesas vazias no spinner
        mesaSpinnerAdapter = new MesaSpinnerAdapter(BarActivity.this, Spinner.MODE_DROPDOWN, pListaMesa);

        // adiciono um layout ao menu de dropdown do spinner
        mesaSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // seto o adapter ao spinner
        spMainMesa.setAdapter(mesaSpinnerAdapter);

    }

    private void initEvents() {
        this.spMainMesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                litarItensDoPedido();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(BarActivity.this, "Selecione uma mesa para ver seus itens", Toast.LENGTH_LONG).show();
            }

        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * preenche o listview com os dados da lista de produtos
     */
    private void popularListViewItensPedido(List<ItemDoPedido> pItensDoPedido) {

        //crio o adaptar para o list view
        this.itensDoPedidoAdapter = new ItensDoPedidoAdapter(BarActivity.this, R.layout.list_view_itens_pedido, pItensDoPedido);

        //seto o adapter ao list view
        this.lvItensDoPedido.setAdapter(this.itensDoPedidoAdapter);
    }

    /**
     * Inicializa os objetos
     */
    private void iniciarObjetos() {
        this.tvBarMesa = (TextView) findViewById(R.id.tvBarMesa);
        this.lvItensDoPedido = (ListView) findViewById(R.id.lvBarPedidosFeitos);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        this.spMainMesa = (Spinner) findViewById(R.id.spMainMesa);
    }

    /**
     * inicializo os intents para troca de activity
     */
    private void iniciarIntents() {
        this.intentCardapio = new Intent(BarActivity.this, CardapioActivity.class);
    }

    /**
     * chamo a activity cardapio
     *
     * @param view
     */
    public void eventBarCardapio(View view) {

        Mesa mesaSelecionada = (Mesa) this.spMainMesa.getSelectedItem();

        this.intentCardapio.putExtras(this.criarBundleMesa(mesaSelecionada));

        startActivity(this.intentCardapio);
    }

    private Bundle criarBundleMesa(Mesa pMesa) {
        Bundle bundleMesa = new Bundle();
        bundleMesa.putLong("mesCodigo", pMesa.getMeCodigo());

        return bundleMesa;
    }

//    /**
//     * chamo a activity cardapio
//     *
//     * @param view
//     */
//    public void eventBarAtualizar(View view) {
//        this.litarItensDoPedido();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sobre) {
            //inicio a intent sobre o software
            startActivity(new Intent(getApplicationContext(), SobreActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }


    @Override
    public void onRefresh() {
        this.litarItensDoPedido();
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
