package br.com.blbbb.blbb.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexaoSQLite extends SQLiteOpenHelper {

    // padrao singleton
    private static ConexaoSQLite INSTANCIA_CONEXAO;
    // Versao do banco
    private static final int VERSAO_DB = 2;
    // Nome do banco
    private static final String NOME_DB = "blvendas_android";

    /**
     * Construtor
     *
     * @param context
     */
    private ConexaoSQLite(Context context) {
        super(context, NOME_DB, null, VERSAO_DB);
    }

    /**
     * Construtor (Singleton)
     *
     * @param context
     * @return void
     */
    public static synchronized ConexaoSQLite getInstancia(Context context) {
        if (INSTANCIA_CONEXAO == null) {
            INSTANCIA_CONEXAO = new ConexaoSQLite(context);
        }
        return INSTANCIA_CONEXAO;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL para criar as tabelas

        // tabela CONFIG
        String sqlTabelaConfig = "CREATE TABLE IF NOT EXISTS tbl_config"
                + "("
                + "conf_url_servidor TEXT"
                + ");";

        // tabela USUARIO GARCOM
        String sqlTabelaLoginsLog = "CREATE TABLE IF NOT EXISTS tbl_log_login"
                + "("
                + "conf_garcom_codigo INTEGER"
                + ");";

        db.execSQL(sqlTabelaConfig);

        db.execSQL(sqlTabelaLoginsLog);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // atualiza o banco de dados quando a constante VERSAO_DB e aumentada
        if (newVersion > oldVersion) {
        }
    }

}
