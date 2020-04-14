

package br.com.blbbb.blbb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import br.com.blbbb.blbb.activity.BarActivity;
import br.com.blbbb.blbb.activity.OpcoesActivity;
import br.com.blbbb.blbb.config.Config;
import br.com.blbbb.blbb.dbHelper.ConexaoSQLite;
import br.com.blbbb.blbb.model.Garcom;
import br.com.blbbb.blbb.util.BLAlarm;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    //componentes gui
    private EditText pfMainSenha; //senha do garcon
    private EditText etMainLogin;//login do garcom (primeiro nome)
    private Switch swtNotificacoes;

    private BLAlarm blAlarm;

    //intents
    private Intent intentOpcoes;
    private Intent intentBar;

    //para conexao ao webservice
    private RequestQueue mRequestQueue;
    private static final String TAG_QUEUE = "queue_wbs_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.iniciarObjetosGui();
        this.iniciarEventos();
        this.iniciarIntents();
        this.configurar();


        //verifico se o sistema esta configurado com url de conexao ao servidor gravada no banco
        if (Config.urlBase != null) {
            Log.d(TAG, "URLBASE" + Config.urlBase);
            Log.d(TAG, Config.garconCodigo + "q");
            Config.urlBase = Config.getConfigUrlBase(ConexaoSQLite.getInstancia(MainActivity.this));
        } else { //sistema nao esta configurado
            Toast.makeText(MainActivity.this, "Configure o sistema!", Toast.LENGTH_LONG).show();

            // inicio a activity de opcoes
            startActivity(this.intentOpcoes);
        }

    }

    /**
     * Inicializa objetos da interface grafica
     */
    private void iniciarObjetosGui() {
        this.etMainLogin = (EditText) findViewById(R.id.etMainLogin);
        this.pfMainSenha = (EditText) findViewById(R.id.etMainSenha);
        this.swtNotificacoes = (Switch) findViewById(R.id.swtNotificacoes);
    }

    private void iniciarEventos() {
        if (this.swtNotificacoes != null) {
            this.swtNotificacoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        blAlarm = new BLAlarm(MainActivity.this);
                        Toast.makeText(MainActivity.this, "Notificações Ativadas!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (null != blAlarm) {
                            boolean cancelou = blAlarm.cancelarAlarme(MainActivity.this);
                            if (cancelou) {
                                Toast.makeText(MainActivity.this, "Notificações desativadas!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * inicializo os intents para troca de activity
     */
    private void iniciarIntents() {
        this.intentOpcoes = new Intent(MainActivity.this, OpcoesActivity.class);
        this.intentBar = new Intent(MainActivity.this, BarActivity.class);
    }

    /**
     * Configuro o sistema na inicializacao
     *
     * @return Config
     */
    private void configurar() {
        // inicio o banco de dados
        ConexaoSQLite.getInstancia(MainActivity.this);

        //recupero a urlBase do banco
        Config.urlBase = Config.getConfigUrlBase(ConexaoSQLite.getInstancia(MainActivity.this));

        if (null == blAlarm) {
            blAlarm = new BLAlarm(this);
        }
    }

    /**
     * Click no botao Entrar
     *
     * @param view
     */
    public void eventMainEntrar(View view) {

        //valido o formulario
        if (this.validarFormulario()) {

            //recupero os dados do formulario
            //mesa do spinner
//            final Mesa mesa = this.mesaSpinnerAdapter.getMesa(this.spMainMesa.getSelectedItemPosition());

            //dados do garcom
            final Garcom garcom = new Garcom();
            garcom.setGaNome(this.etMainLogin.getText().toString());
            garcom.setGaSenha(this.pfMainSenha.getText().toString());

            //requisicao usando o volley do google
            this.mRequestQueue = Volley.newRequestQueue(this);
            String url = Config.urlBase + "post/postLogin.php?action=logar";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {



                        @Override
                        public void onResponse(String response) {

                            ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstancia(MainActivity.this);

                            //login ok no servidor - codigo do garcom logado
                            long respostaCodigoGarcom = 0;

                            try {
                                respostaCodigoGarcom = Long.parseLong(response.trim());
                            } catch (NumberFormatException e) {
                                Log.d("MAIN", response);
                                Toast.makeText(MainActivity.this, "Não foi possível comunicar com o servidor. Endereço correto???" + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                            if (respostaCodigoGarcom > 0) {

                                //seto as informacoes globais do sistema
                                Config.garconCodigo = respostaCodigoGarcom;

                                long garconCodigo = Config.getConfigGarcomCodigo(conexaoSQLite);

                                //verifica se ja existe garcon logado pelo menos uma vez
                                if (garconCodigo > 0) {
                                    //update no garcom que logou
                                    Config.updateConfigGarcomLogado(conexaoSQLite, Config.garconCodigo);
                                } else {
                                    //update no garcom que logou
                                    Config.salvarGarconLogin(conexaoSQLite, Config.garconCodigo);
                                }
                                // inicio a activity destino
                                startActivity(intentBar);

                            } else {
                                //erro de login no servidor
                                Toast.makeText(MainActivity.this, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Não foi possível conectar a " + Config.getUrlBase(ConexaoSQLite.getInstancia(MainActivity.this)), Toast.LENGTH_LONG).show();
                    System.out.println("Response is: " + error.getMessage());
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("postGaNome", garcom.getGaNome());
                    params.put("postGaSenha", garcom.getGaSenha());

                    return params;
                }
            };

            // Set the tag on the request.
            stringRequest.setTag(TAG_QUEUE);

            // Add the request to the RequestQueue.
            mRequestQueue.add(stringRequest);

        } else {

            //erro no preenchimento do formulario
            Toast.makeText(MainActivity.this, "Existem campos vazios ou mal preenchidos no formulario!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Valida o formulario
     *
     * @return boolean
     */
    private boolean validarFormulario() {

        if (this.etMainLogin.getText().length() == 0
                || this.pfMainSenha.getText().length() == 0) {
//                || this.spMainMesa.getSelectedItemPosition() < 0) {
            return false;
        }

        return true;
    }

    /**
     * Click no botao Opcoes
     *
     * @param view
     */
    public void eventMainOpcoes(View view) {
        // inicio a activity destino
        startActivity(this.intentOpcoes);

    }

    /**
     * recria a activity para tentar conexao novamente
     *
     * @param view
     */
    public void eventMainRestart(View view) {
        recreate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(TAG_QUEUE);
        }
    }

}
