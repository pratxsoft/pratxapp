package br.com.blbbb.blbb.config;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.blbbb.blbb.dbHelper.ConexaoSQLite;

public class Config {

    //url base dos scripts php
    public static String urlBase;

    //codigo do garcom que atende a mesa
    public static long garconCodigo;

    // nome da tabela de config
    private static final String TBL_CONFIG = "tbl_config";
    private static final String TBL_LOG_USUARIO = "tbl_log_login";

    // nome das colunas da tabela config
    private static final String KEY_URL_SERVIDOR = "conf_url_servidor";
    private static final String KEY_GARCON_CODIGO = "conf_garcom_codigo";

    // colunas da tabela
    private static final String[] COLUNAS_TBL_CONFIG = {
            KEY_URL_SERVIDOR
    };
    private static final String[] COLUNAS_TBL_USUARIO = {
            KEY_GARCON_CODIGO
    };

    public static boolean salvarConfig(ConexaoSQLite pSQLite, String pUrlBase) {

        // recupera a instancia do banco para efetuar escrita
        SQLiteDatabase db = pSQLite.getWritableDatabase();

        try {

            // Cria os contentValues (nomes das colunas das tabelas e
            ContentValues values = new ContentValues();

            // valores que serao inseridos no banco de dados)
            values.put(KEY_URL_SERVIDOR, pUrlBase);
            // executa o insert no banco
            db.insert(TBL_CONFIG, // nome da tabela
                    null, // nullColumnHack
                    values); // key/valores

        } catch (Exception e) {
            Log.d("DB_ERROR", "NAO FOI POSSIVEL INSERIR CONFIG");
            e.printStackTrace();
            return false;
        } finally {
            // fecha a conexao
            if (db.isOpen()) {
                db.close();
            }
        }
        return true;

    }

    public static String getUrlBase(ConexaoSQLite pSQLite){
        if(null != Config.urlBase){
            return Config.urlBase;
        }else{
            return getConfigUrlBase(pSQLite);
        }
    }

    /**
     * Retorna um config da base de dados
     */
    public static String getConfigUrlBase(ConexaoSQLite pSQLite) {

        // recupera a instancia do banco para efetuar apenas leituras
        SQLiteDatabase db = pSQLite.getReadableDatabase();

        // cursor para movimentar entre os resultados da query
        Cursor cursor = null;

        try {

            // constroi a consulta (query)
            // //veja outro modo de consulta no metodo getListaConfigDAO
            cursor = db.query(TBL_CONFIG, // a. tabela
                    COLUNAS_TBL_CONFIG, // b. nomes das colunas
                    null, // c. selections (where)
                    null, // d.	// selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit

            // vai para o primeiro resultado, caso exista
            if (cursor != null) {
                cursor.moveToFirst();
            }

            // cria um config com os dados vindos do banco
            return cursor.getString(0);

        } catch (Exception e) {
            Log.d("DB_ERROR", "NAO FOI POSSIVEL RECUPERAR O CONFIG");
        } finally {
            if (cursor != null) {
                // fecho o cursor
                cursor.close();

            }
            if (db != null) {
                // fecho a conexao
                db.close();

            }
        }

        return null;
    }


    public static boolean salvarGarconLogin(ConexaoSQLite pSQLite, long pGarcomLogadoCodigo) {

        // recupera a instancia do banco para efetuar escrita
        SQLiteDatabase db = pSQLite.getWritableDatabase();

        try {

            // Cria os contentValues (nomes das colunas das tabelas e
            ContentValues values = new ContentValues();

            // valores que serao inseridos no banco de dados)
            values.put(KEY_GARCON_CODIGO, pGarcomLogadoCodigo);
            // executa o insert no banco
            db.insert(TBL_LOG_USUARIO, // nome da tabela
                    null, // nullColumnHack
                    values); // key/valores

        } catch (Exception e) {
            Log.d("DB_ERROR", "NAO FOI POSSIVEL INSERIR USUARIO");
            e.printStackTrace();
            return false;
        } finally {
            // fecha a conexao
            if (db.isOpen()) {
                db.close();
            }
        }
        return true;

    }

    /**
     * Atualizar um config da base de dados
     *
     * @return boolean
     */
    public static boolean updateConfigGarcomLogado(ConexaoSQLite pSQLite, long pGarcomLogadoCodigo) {

        SQLiteDatabase db = null;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GARCON_CODIGO, pGarcomLogadoCodigo);

        try {
            // recupera uma instancia de escrita no banco
            db = pSQLite.getWritableDatabase();

            // executa o update no banco
            db.update(
                    TBL_LOG_USUARIO,
                    contentValues,
                    null,
                    null
            );

        } catch (Exception e) {
            Log.d("DB_ERROR", "NAO FOI POSSIVEL ATUALIZAR CONFIG");
            return false;
        } finally {
            // fecha a conexao
            db.close();
        }
        return true;
    }


    /**
     * Retorna um config da base de dados
     */
    public static long getConfigGarcomCodigo(ConexaoSQLite pSQLite) {

        // recupera a instancia do banco para efetuar apenas leituras
        SQLiteDatabase db = pSQLite.getReadableDatabase();

        // cursor para movimentar entre os resultados da query
        Cursor cursor = null;

        try {

            // constroi a consulta (query)
            // //veja outro modo de consulta no metodo getListaConfigDAO
            cursor = db.query(TBL_LOG_USUARIO, // a. tabela
                    COLUNAS_TBL_USUARIO, // b. nomes das colunas
                    null, // c. selections (where)
                    null, // d.	// selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit

            // vai para o primeiro resultado, caso exista
            if (cursor != null) {
                cursor.moveToFirst();
            }

            // cria um config com os dados vindos do banco
            return cursor.getLong(0);

        } catch (Exception e) {
            Log.d("DB_ERROR", "NAO FOI POSSIVEL RECUPERAR O GARCOM");
        } finally {
            // fecho o cursor
            cursor.close();
            // fecho a conexao
            db.close();
        }

        return 0;
    }

    /**
     * Exclui um config da base de dados
     *
     * @return boolean
     */
    public static boolean excluirConfigDAO(ConexaoSQLite pSQLite) {

        SQLiteDatabase db = null;
        try {
            // recupera uma instancia de escrita no banco
            db = pSQLite.getWritableDatabase();

            // executa o delete no banco
            db.delete(TBL_CONFIG, null, null);

        } catch (Exception e) {
            Log.d("DB_ERROR", "NAO FOI POSSIVEL DELETAR CONFIG");
            return false;
        } finally {
            // fecha a conexao
            db.close();
        }
        return true;
    }

}
