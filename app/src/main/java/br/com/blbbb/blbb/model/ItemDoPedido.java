package br.com.blbbb.blbb.model;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

/**
 * Created by bruno on 07/04/2015.
 */
public class ItemDoPedido {

    private long codigo;
    private long mesaCodigo;
    private Produto produto;
    private String status;
    private String observacao;
    private float quantidade;

    public ItemDoPedido() {
    }


    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public long getMesaCodigo() {
        return mesaCodigo;
    }

    public void setMesaCodigo(long mesaCodigo) {
        this.mesaCodigo = mesaCodigo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public String exibirItem() {
        return produto.getCodigo() + " - " +
                produto.getNome() + " x " + quantidade + "\n" +
                "UN R$ " + produto.getValor().getMoney() +
                "  --  TOTAL R$ " + getPrecoTotalItem(produto.getValor().getMoney(), quantidade) +
                observacao + "\n    -------------    \n";
    }

    public BigDecimal getPrecoTotalItem(BigDecimal pValorUnitario, float pQuantidade) {
        return pValorUnitario.multiply(new BigDecimal(pQuantidade)).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return produto.getNome();
    }

    public void imprimir() {
        System.out.println(produto.toString());
    }

}