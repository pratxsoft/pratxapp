package br.com.blbbb.blbb.model;

/**
 * Created by bruno on 25/03/2015.
 */
public class Mesa {

    private long meCodigo;
    private String meMesa;
    private String meSituacao;

    public Mesa(){}

    public Mesa(long meCodigo, String meMesa, String meSituacao) {
        this.meCodigo = meCodigo;
        this.meMesa = meMesa;
        this.meSituacao = meSituacao;
    }

    public long getMeCodigo() {
        return meCodigo;
    }

    public void setMeCodigo(long meCodigo) {
        this.meCodigo = meCodigo;
    }

    public String getMeMesa() {
        return meMesa;
    }

    public void setMeMesa(String meMesa) {
        this.meMesa = meMesa;
    }

    public String getMeSituacao() {
        return meSituacao;
    }

    public void setMeSituacao(String meSituacao) {
        this.meSituacao = meSituacao;
    }

    @Override
    public String toString() {
        return  meMesa + " - " + meSituacao;
    }
}