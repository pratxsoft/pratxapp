package br.com.blbbb.blbb.model;

/**
 * Created by bruno on 27/03/2015.
 */
public class Garcom {

    private long gaCodigo;
    private String gaNome;
    private String gaSenha;
    private String gaEndereco;
    private String gaBairro;
    private long ciCodigo;
    private long esCodigo;
    private String gaCEP;
    private String gaTelefone;
    private float gaComissao;

    public long getGaCodigo() {
        return gaCodigo;
    }

    public void setGaCodigo(long gaCodigo) {
        this.gaCodigo = gaCodigo;
    }

    public String getGaNome() {
        return gaNome;
    }

    public void setGaNome(String gaNome) {
        this.gaNome = gaNome;
    }

    public String getGaSenha() {
        return gaSenha;
    }

    public void setGaSenha(String gaSenha) {
        this.gaSenha = gaSenha;
    }

    public String getGaEndereco() {
        return gaEndereco;
    }

    public void setGaEndereco(String gaEndereco) {
        this.gaEndereco = gaEndereco;
    }

    public String getGaBairro() {
        return gaBairro;
    }

    public void setGaBairro(String gaBairro) {
        this.gaBairro = gaBairro;
    }

    public long getCiCodigo() {
        return ciCodigo;
    }

    public void setCiCodigo(long ciCodigo) {
        this.ciCodigo = ciCodigo;
    }

    public long getEsCodigo() {
        return esCodigo;
    }

    public void setEsCodigo(long esCodigo) {
        this.esCodigo = esCodigo;
    }

    public String getGaCEP() {
        return gaCEP;
    }

    public void setGaCEP(String gaCEP) {
        this.gaCEP = gaCEP;
    }

    public String getGaTelefone() {
        return gaTelefone;
    }

    public void setGaTelefone(String gaTelefone) {
        this.gaTelefone = gaTelefone;
    }

    public float getGaComissao() {
        return gaComissao;
    }

    public void setGaComissao(float gaComissao) {
        this.gaComissao = gaComissao;
    }

    @Override
    public String toString() {
        return gaCodigo + " - " + gaNome;
    }
}
