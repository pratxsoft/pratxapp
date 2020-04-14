
package br.com.blbbb.blbb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.blbbb.blbb.R;
import br.com.blbbb.blbb.config.Config;
import br.com.blbbb.blbb.dbHelper.ConexaoSQLite;

public class OpcoesActivity extends AppCompatActivity {

    private EditText edtConfUrlServidor;
    //    private EditText edtConfPortaServidor;
    private Button btnConfSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstancia(OpcoesActivity.this);


        //controller para salvar as configs no banco
        //edit text da url
        this.edtConfUrlServidor = (EditText) findViewById(R.id.edtConfUrlServidor);
//        this.edtConfPortaServidor = (EditText) findViewById(R.id.edtConfPortaServidor);

        //botao salvar configuracoes
        this.btnConfSalvar = (Button) findViewById(R.id.btnConfSalvar);

        //clicklistener do botao salvar
        this.btnConfSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

//                String porta = "";
//
//                if (!edtConfPortaServidor.getText().equals("")) {
//                    porta = edtConfPortaServidor.getText().toString();
//                }
                Config.urlBase = "http://" + edtConfUrlServidor.getText().toString() + "/BLBarWEBSServer/";


                //deletada antes de inserir a nova config(nao tem update)
                Config.excluirConfigDAO(conexaoSQLite);

                //salvo as config
                if (Config.salvarConfig(conexaoSQLite, Config.urlBase)) {

                    //seto o endereco base do servidor (url base)

                    Log.d("URL_BASE", Config.urlBase);
                    Log.d("URL_BASE_DB", Config.getConfigUrlBase(ConexaoSQLite.getInstancia(OpcoesActivity.this)));

                    Toast.makeText(OpcoesActivity.this, "Configuracoes salvas com sucesso", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OpcoesActivity.this, "Erro ao salvar Configuracoes", Toast.LENGTH_LONG).show();
                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config, menu);
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
}
