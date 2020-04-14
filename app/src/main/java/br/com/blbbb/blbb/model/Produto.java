package br.com.blbbb.blbb.model;

import br.com.blbbb.blbb.util.Money;

/**
 * Created by bruno on 31/03/2015.
 */
public class Produto {

    private long codigo;
    private String nome;
    private String Descricao;
    private long codigoFornecedor;
    private Money valor;
    private float estoque;
    private String codigoBarras;
    private long codigoUnidadeMedida;
    private String observacao;

    public Produto(){}

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public long getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(long codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    public Money getValor() {
        return valor;
    }

    public void setValor(Money valor) {
        this.valor = valor;
    }

    public void setValor(String valor) {
        this.valor = new Money(valor);
    }

    public float getEstoque() {
        return estoque;
    }

    public void setEstoque(float estoque) {
        this.estoque = estoque;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public long getCodigoUnidadeMedida() {
        return codigoUnidadeMedida;
    }

    public void setCodigoUnidadeMedida(long codigoUnidadeMedida) {
        this.codigoUnidadeMedida = codigoUnidadeMedida;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
