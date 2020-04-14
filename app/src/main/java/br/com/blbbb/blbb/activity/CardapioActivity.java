package br.com.blbbb.blbb.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.blbbb.blbb.R;
import br.com.blbbb.blbb.adapter.list.CardapioAdapter;
import br.com.blbbb.blbb.comparators.ItemPedidoComparatorPorCodigo;
import br.com.blbbb.blbb.comparators.ItemPedidoComparatorPorCodigoDescendente;
import br.com.blbbb.blbb.comparators.ItemPedidoComparatorPorNome;
import br.com.blbbb.blbb.comparators.ItemPedidoComparatorPorNomeDescendente;
import br.com.blbbb.blbb.config.Config;
import br.com.blbbb.blbb.dbHelper.ConexaoSQLite;
import br.com.blbbb.blbb.model.ItemDoPedido;
import br.com.blbbb.blbb.model.Mesa;
import br.com.blbbb.blbb.model.Produto;

public class CardapioActivity extends AppCompatActivity {

    //objetos
    private List<ItemDoPedido> listaItensDoCardapio;
    private Mesa mesaDoPedido = new Mesa();

    //gui componentes
    private ListView lvCardapio;
    private EditText edtFiltro;

    //adapters
    private CardapioAdapter cardapioAdapter;

    //para conexao ao webservice
    private RequestQueue mRequestQueue;
    private static final String TAG = "queue_wbs_cardapio";
    private final String URL_CARDAPIO = Config.getUrlBase(ConexaoSQLite.getInstancia(CardapioActivity.this)) + "post/postProduto.php?action=listar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        this.iniciarObjetos();
        this.iniciarGUIObjetos();
        this.listarCardapio(URL_CARDAPIO);
        this.iniciarListenner();

    }

    private Mesa getBundleMesa() {
        // Get passed values
        Bundle extras = getIntent().getExtras();
        Mesa mesa = new Mesa();
        mesa.setMeCodigo(extras.getLong("mesCodigo"));

        return mesa;
    }

    /**
     * inicializa os objetos
     */
    private void iniciarObjetos() {

        //requisicao usando o volley do google
        this.mRequestQueue = Volley.newRequestQueue(this);
        this.listaItensDoCardapio = new ArrayList<>();
        this.mesaDoPedido = this.getBundleMesa();
    }

    /**
     * inicializo os objetos gui
     */
    private void iniciarGUIObjetos() {
        this.lvCardapio = (ListView) findViewById(R.id.lvCardapio);
        this.edtFiltro = (EditText) findViewById(R.id.edtFiltro);


        //hobilita o filtro do listview
        this.lvCardapio.setTextFilterEnabled(true);
    }

    /**
     * Inicializo os listenners dos componentes
     */
    private void iniciarListenner() {
        //adiciono listenner de click ao listview
        this.lvCardapio.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                final ItemDoPedido itemDoPedidoSelecionado = (ItemDoPedido) adapter.getItemAtPosition(position);

                alterarItemPedido(itemDoPedidoSelecionado, position);
            }
        });

        this.edtFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                cardapioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CardapioActivity.this.cardapioAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                cardapioAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Altera as informacoes de algum item do pedido
     *
     * @param pItemDoPedido
     * @param pIndex
     */
    private void alterarItemPedido(final ItemDoPedido pItemDoPedido, final int pIndex) {

        // cria um alert de input
        final Dialog dialogAlterarItemCardapio = new Dialog(CardapioActivity.this);

        // seta o titulo do dialog
        dialogAlterarItemCardapio.setTitle(pItemDoPedido.getProduto().getNome());

        // seto a view  (layout arquivo dialog_item_cadapio.xml)
        dialogAlterarItemCardapio.setContentView(R.layout.dialog_item_cardapio);

        //crio os inputs
        //quantidade
        final TextView tvItemCardapioQuantidade = (TextView) dialogAlterarItemCardapio.findViewById(R.id.etItemCardapioQuantidade);

        //Observacao
        final TextView tvItemCardapioObservacao = (TextView) dialogAlterarItemCardapio.findViewById(R.id.etItemCardapioOBS);

        //crio o botao
        final Button btItemCardapioOk = (Button) dialogAlterarItemCardapio.findViewById(R.id.btItemCardapioOk);
        final Button btItemCardapioAdd = (Button) dialogAlterarItemCardapio.findViewById(R.id.btItemCardapioAdd);
        final Button btItemCardapioRem = (Button) dialogAlterarItemCardapio.findViewById(R.id.btItemCardapioRem);
        final Button btItemCardapioAddMeio = (Button) dialogAlterarItemCardapio.findViewById(R.id.btItemCardapioAddMeio);
        final Button btItemCardapioRemMeio = (Button) dialogAlterarItemCardapio.findViewById(R.id.btItemCardapioRemMeio);

        //caso o item do pedido ja tenha dados, passo eles para o dialog ja preenchido
        tvItemCardapioQuantidade.setText("0");
        if (pItemDoPedido.getQuantidade() > 0) {
            tvItemCardapioQuantidade.setText(String.valueOf(pItemDoPedido.getQuantidade()));
        }
        if (pItemDoPedido.getObservacao().trim().length() > 0) {
            tvItemCardapioObservacao.setText(pItemDoPedido.getObservacao());
        }

        //adiciono listener ao botao ok
        btItemCardapioOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //altero os dados do item do cardapio
                    pItemDoPedido.setQuantidade(Float.parseFloat(tvItemCardapioQuantidade.getText().toString()));
                    pItemDoPedido.setObservacao(tvItemCardapioObservacao.getText().toString());
                    pItemDoPedido.setStatus("Em aberto");

                    //atualizo o item na lista de pedidos (interface do cardapio
                    listaItensDoCardapio.set(pIndex, pItemDoPedido);

                    //atualizou ou adiciono o item a lista do pedido a enviar para cozinha
                    if (cardapioAdapter.existeOItem(pItemDoPedido)) {
                        cardapioAdapter.removerItemDoPedido(pItemDoPedido);
                    }
                    cardapioAdapter.addItemDoPedido(pItemDoPedido);

                    //aviso ao adapter para atualizar o listView do cardapio com as novas informacoes de quantidade e observacao
                    cardapioAdapter.notifyDataSetChanged();

                } catch (NumberFormatException e) {
                    Toast.makeText(CardapioActivity.this, "Nenhum Item selecionado", Toast.LENGTH_LONG).show();
                }

                //removo o dialog
                dialogAlterarItemCardapio.dismiss();
            }
        });

        btItemCardapioAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float quantidade = Float.parseFloat(tvItemCardapioQuantidade.getText().toString());
                quantidade++;
                tvItemCardapioQuantidade.setText(String.valueOf(quantidade));
            }
        });

        btItemCardapioRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float quantidade = Float.parseFloat(tvItemCardapioQuantidade.getText().toString());
                quantidade--;
                if (quantidade < 0) {
                    quantidade = 0.0f;
                }
                tvItemCardapioQuantidade.setText(String.valueOf(quantidade));
            }
        });

        btItemCardapioAddMeio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float quantidade = Float.parseFloat(tvItemCardapioQuantidade.getText().toString());
                quantidade += 0.5f;
                tvItemCardapioQuantidade.setText(String.valueOf(quantidade));
            }
        });

        btItemCardapioRemMeio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float quantidade = Float.parseFloat(tvItemCardapioQuantidade.getText().toString());
                quantidade -= 0.5f;
                if (quantidade < 0) {
                    quantidade = 0.0f;
                }
                tvItemCardapioQuantidade.setText(String.valueOf(quantidade));
            }
        });

        //exibo o dialog criado
        dialogAlterarItemCardapio.show();
    }

    /**
     * Reinicia a lista de itens do pedido
     */
    private void reiniciarListaDeItensDoPedido() {
        this.listarCardapio(URL_CARDAPIO);
    }

    /**
     * preenche o listview com os dados da lista de produtos
     */
    private void popularListViewCardapio() {

        //crio o adaptar para o list view
        this.cardapioAdapter = new CardapioAdapter(CardapioActivity.this, R.layout.list_view_cardapio, this.listaItensDoCardapio);

        //seto o adapter ao list view
        this.lvCardapio.setAdapter(this.cardapioAdapter);
    }

    private void listarCardapio(String pUrl) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, pUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        ItemDoPedido itemDoPedido;
                        Produto produtoDoPedido;
                        String strProduto[];
                        String strCardapio[];
                        listaItensDoCardapio = new ArrayList<>();

                        try {

//                            Log.d("CARDÁPIO", response.toString());

                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray arrayProdutosDoCardapio = jsonObject.getJSONArray("lista_produtos");
                            Mesa mesa = null;
                            JSONObject produtoCardapioJson = null;

                            for (int i = 0; i < arrayProdutosDoCardapio.length(); i++) {

                                produtoCardapioJson = arrayProdutosDoCardapio.getJSONObject(i);

                                produtoDoPedido = new Produto();
                                produtoDoPedido.setCodigo(produtoCardapioJson.getLong("id"));
                                produtoDoPedido.setNome(produtoCardapioJson.getString("nome"));
//                                produtoDoPedido.setDescricao(produtoCardapioJson.getString("descricao"));
                                produtoDoPedido.setDescricao("");//foi removido na versão 4
                                produtoDoPedido.setValor(produtoCardapioJson.getString("valor"));
                                produtoDoPedido.setEstoque(produtoCardapioJson.getInt("estoque"));

                                itemDoPedido = new ItemDoPedido();
                                itemDoPedido.setProduto(produtoDoPedido);

                                //adiciono o produto a lista
                                listaItensDoCardapio.add(itemDoPedido);

                            }

                            //preencho o spinner com as mesas vindas do servidor
                            popularListViewCardapio();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding request to request mRequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * clique no botao de exibir os itens do pedido
     *
     * @param view
     */
    public void eventCardapioPedidoItens(View view) {
        exibirItensDoPedido(listarItensDoPedido());
    }

    private String listarItensDoPedido() {

        BigDecimal totalPedido = new BigDecimal("0");
        String itensDoPedido = "";
        //crio o texto com os itens do pedido

//        listaItensDoCardapio = new ArrayList<>(new HashSet<>(listaItensDoCardapio));

//        Log.d("ITEN_CARDA", cardapioAdapter.getItemDoPedidoList().size() + "");

        for (int i = 0; i < cardapioAdapter.getItemDoPedidoList().size(); i++) {
            if (cardapioAdapter.getItemDoPedidoList().get(i).getQuantidade() > 0) {

                itensDoPedido += cardapioAdapter.getItemDoPedidoList().get(i).exibirItem();
                totalPedido = totalPedido.add(
                        cardapioAdapter.getItemDoPedidoList().get(i).getPrecoTotalItem(
                                cardapioAdapter.getItemDoPedidoList().get(i).getProduto().getValor().getMoney(),
                                cardapioAdapter.getItemDoPedidoList().get(i).getQuantidade()
                        )
                );
            }
        }
        return itensDoPedido + "\n\nTOTAL DO PEDIDO: R$ " + totalPedido;
    }

    /**
     * exibe a lista de itens do pedido
     */
    private void exibirItensDoPedido(String pitensDoPedido) {


        // cria um alert de input
        AlertDialog.Builder alert = new AlertDialog.Builder(CardapioActivity.this);

        // seta o titulo do alert
        alert.setTitle("Itens do pedido");

        // mensagem do alert
        alert.setMessage(pitensDoPedido);


        // crio o botao de ok
        alert.setPositiveButton("Ok", null);

        // exibo o alert
        alert.show();

    }

    /**
     * Click no botao de finalizar o pedido
     */
    public void eventCardapioFecharPedido(View view) {

        //verifico se existem itens no pedido
        if (pedidoVazio()) {
            Toast.makeText(CardapioActivity.this, "Adicione itens ao pedido antes de finalizar!", Toast.LENGTH_LONG).show();
        } else {
            // cria um alert de input
            AlertDialog.Builder alert = new AlertDialog.Builder(CardapioActivity.this);

            // seta o titulo do alert
            alert.setTitle("Fechar pedido?");

            // mensagem do alert
            alert.setMessage("Apos o fechamento do pedido, este nao podera ser alterado. Deseja continuar?");

            // crio o botao cancelar
            alert.setNegativeButton("Nao", null);

            // crio o botao de ok
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    enviarPedido(cardapioAdapter.getItemDoPedidoList());
                }
            });

            // exibo o alert
            alert.show();
        }
    }

    /**
     * Envia um pedido fechado para a cozinha
     *
     * @param pListItensDoCardapio
     */
    private void enviarPedido(final List<ItemDoPedido> pListItensDoCardapio) {


        //requisicao usando o volley do google
        this.mRequestQueue = Volley.newRequestQueue(this);
        String url = Config.getUrlBase(ConexaoSQLite.getInstancia(CardapioActivity.this)) + "post/postPedido.php?action=pedir";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("CARDAPIO RESP: ", response);

                        try {
                            int resposta = Integer.parseInt(response);

                            if (resposta == 1) {
                                reiniciarListaDeItensDoPedido();
                                Toast.makeText(CardapioActivity.this, "Pedido enviado para a cozinha!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CardapioActivity.this, "Erro ao fazer pedido para a cozinha", Toast.LENGTH_LONG).show();
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(CardapioActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CardapioActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Response is: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                ItemDoPedido itemDoPedido;
                List<ItemDoPedido> itensPedidos = new ArrayList<>();

                //crio uma lista apenas com os itens do cardápio que o usuário selecionou
                //itens de pedido com quantidade = 0 serao descartados
                for (int i = 0; i < pListItensDoCardapio.size(); i++) {

                    itemDoPedido = pListItensDoCardapio.get(i);

                    if (itemDoPedido.getQuantidade() > 0) {
                        itemDoPedido.setStatus(getString(R.string.status_em_pedido));
                        itensPedidos.add(itemDoPedido);
                    }
                }

                long garcomCodigo = Config.getConfigGarcomCodigo(ConexaoSQLite.getInstancia(CardapioActivity.this));

                //crio o json com os itens do pedido para enviar ao servidor
                //uso a lib Gson do google
                String itensDoPedidoJson = new Gson().toJson(itensPedidos);

                //seto os dados que irao para o servidor
                Map<String, String> params = new HashMap<String, String>();
//                params.put("postQuantidadeItens", String.valueOf(pListItensDoCardapio.size()));
                params.put("postMesaNumero", String.valueOf(mesaDoPedido.getMeCodigo()));
                params.put("postItensPedido", itensDoPedidoJson);
                params.put("postGarcomCodigo", String.valueOf(garcomCodigo));

                return params;
            }
        };

        // Set the tag on the request.
        stringRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        this.mRequestQueue.add(stringRequest);

    }

    private boolean pedidoVazio() {
        for (int i = 0; i < listaItensDoCardapio.size(); i++) {
            if (listaItensDoCardapio.get(i).getQuantidade() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cardapio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_ordenar_codigo) {
            this.ordernarPorCodigoAsc(this.listaItensDoCardapio);
            return true;
        } else if (id == R.id.action_ordenar_codigo_desc) {
            this.ordernarPorCodigoDesc(this.listaItensDoCardapio);
            return true;
        } else if (id == R.id.action_ordenar_alfabetico) {
            this.ordernarPorNomeAsc(this.listaItensDoCardapio);
            return true;
        } else if (id == R.id.action_ordenar_alfabetico_desc) {
            this.ordernarPorNomeDesc(this.listaItensDoCardapio);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ordernarPorNomeAsc(List<ItemDoPedido> pedidoList) {
        Collections.sort(pedidoList, new ItemPedidoComparatorPorNome());
        this.cardapioAdapter.notifyDataSetChanged();
    }

    private void ordernarPorNomeDesc(List<ItemDoPedido> pListaItemDoPedido) {
        Collections.sort(pListaItemDoPedido, new ItemPedidoComparatorPorNomeDescendente());
        this.cardapioAdapter.notifyDataSetChanged();
    }

    private void ordernarPorCodigoAsc(List<ItemDoPedido> pedidoList) {
        Collections.sort(pedidoList, new ItemPedidoComparatorPorCodigo());
        this.cardapioAdapter.notifyDataSetChanged();
    }

    private void ordernarPorCodigoDesc(List<ItemDoPedido> pedidoList) {
        Collections.sort(pedidoList, new ItemPedidoComparatorPorCodigoDescendente());
        this.cardapioAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(TAG);
        }
    }
}
