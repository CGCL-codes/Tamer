package br.com.geostore.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.jboss.seam.annotations.Name;

@Entity
@Name("cidade")
@Table(name = "gs_cidades")
public class Cidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id_cidade")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_unidade_federacao")
    private UnidadeFederacao unidadeFederacao;

    public Cidade() {
        unidadeFederacao = new UnidadeFederacao();
    }

    public Cidade(String descricao, UnidadeFederacao unidadeFederacao) {
        this.descricao = descricao;
        this.unidadeFederacao = unidadeFederacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UnidadeFederacao getUnidadeFederacao() {
        return unidadeFederacao;
    }

    public void setUnidadeFederacao(UnidadeFederacao unidadeFederacao) {
        this.unidadeFederacao = unidadeFederacao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((unidadeFederacao == null) ? 0 : unidadeFederacao.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Cidade other = (Cidade) obj;
        if (descricao == null) {
            if (other.descricao != null) return false;
        } else if (!descricao.equals(other.descricao)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (unidadeFederacao == null) {
            if (other.unidadeFederacao != null) return false;
        } else if (!unidadeFederacao.equals(other.unidadeFederacao)) return false;
        return true;
    }
}
