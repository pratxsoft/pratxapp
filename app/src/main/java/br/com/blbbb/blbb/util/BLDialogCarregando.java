package br.com.blbbb.blbb.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by bruno on 17/02/2015.
 */
public class BLDialogCarregando {

    private ProgressDialog dialog;
    private final String titulo = "Aguarde";
    private final String mensagem = "Carregando";

    public BLDialogCarregando(Context context){
        this.dialog = new ProgressDialog(context);
        this.getDialog().setTitle(this.titulo);
        this.getDialog().setMessage(this.mensagem);
    }

    public BLDialogCarregando(Context context, String pTitulo, String pMensagem){
        this.dialog = new ProgressDialog(context);
        this.getDialog().setTitle(pTitulo);
        this.getDialog().setMessage(pMensagem);
    }

    public void iniciar(){
        this.getDialog().show();
    }

    public void finalizar(){
        this.getDialog().dismiss();
    }

    public ProgressDialog getDialog() {
        return dialog;
    }
}
