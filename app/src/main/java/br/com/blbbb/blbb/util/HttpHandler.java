package br.com.blbbb.blbb.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Br on 15/09/2016.
 */
public class HttpHandler {

    private final String TAG = HttpHandler.class.getSimpleName();
    private final int CONEXAO_TIMEOUT = 15000;
    private final int READ_CONEXAO_TIMEOUT = 10000;

    public HttpHandler() {
    }

    /**
     * Faz uma requisicao HTTP basica GET para uma url
     *
     * @param pUrl
     * @return String
     */
    public String requisicaoHTTP(String pUrl) {
        InputStream is = null;
        HttpURLConnection httpURLConnection = null;
        String resposta = null;
        try {
            URL url = new URL(pUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_CONEXAO_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONEXAO_TIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            //make some HTTP header nicety
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            httpURLConnection.connect();

            //do somehting with response
            is = httpURLConnection.getInputStream();

            String respostaCripto = convertStreamToString(is);

            Log.d("RESPOSTA CRIPTO", respostaCripto);

            String res = new String(respostaCripto);
            resposta = URLDecoder.decode(res, "UTF-8");

            Log.d("RESPOSTA DESCRIPTO", resposta);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return resposta;
    }

    /**
     * Faz uma requisicao completa POST para uma url - envia uma string
     *
     * @param pUrl
     * @param message
     * @return resposta servidor
     */
    public String requisicaoHTTP(String pUrl, String message) {

        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection httpURLConnection = null;
        String resposta = null;


        //crio a url do servidor
        try {

            //crio a url do servidor
            URL url = new URL(pUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_CONEXAO_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONEXAO_TIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP header nicety
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            httpURLConnection.connect();

            //setup send
            os = new BufferedOutputStream(httpURLConnection.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();

            //do somehting with response
            is = httpURLConnection.getInputStream();

            String respostaCripto = convertStreamToString(is);

            Log.d(TAG, "RESPOSTA CRIPTOGRAFADA = " + respostaCripto);

            String res = new String(respostaCripto);
            resposta = URLDecoder.decode(res, "UTF-8");
            Log.d(TAG, "RESPOSTA DESCRIPTOGRAFADA = " + resposta);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } finally {
            //clean up
            try {
                if (os != null && is != null) {
                    os.close();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return resposta;
    }

    /**
     * Converte um inputStream em String
     *
     * @param is
     * @return String
     */
    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public boolean checkConexao(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
//            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
//            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
//            }

            return true;
        } else {
//            Toast.makeText(context, context.getString(R.string.message_sem_con), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
