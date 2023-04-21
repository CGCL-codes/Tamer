package br.com.rotaestatistica.entities;

import java.sql.Timestamp;
import java.util.Date;

public class TrajetoEntity {

    private long id_Trajeto;

    private Date horarioPartida;

    private Date horarioChegada;

    private long id_EstacaoDePartida;

    private long id_EstacaoDeChegada;

    private long id_Clima;

    private long id_Lotacao;

    private long id_UsuarioCadastrado;

    private int duracao;

    public TrajetoEntity() {
    }

    public long getId_Trajeto() {
        return id_Trajeto;
    }

    public void setId_Trajeto(long id_Trajeto) {
        this.id_Trajeto = id_Trajeto;
    }

    public Date getHorarioChegada() {
        return this.horarioChegada;
    }

    public Timestamp getSQLHorarioChegada() {
        Timestamp dateSql = new Timestamp(this.horarioChegada.getTime());
        return dateSql;
    }

    public void setHorarioChegada(Date horarioChegada) {
        this.horarioChegada = horarioChegada;
    }

    public Date getHorarioPartida() {
        return this.horarioPartida;
    }

    public Timestamp getSQLHorarioPartida() {
        Timestamp dateSql = new Timestamp(horarioPartida.getTime());
        return dateSql;
    }

    public void setHorarioPartida(Date horarioPartida) {
        this.horarioPartida = horarioPartida;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public long getId_EstacaoDePartida() {
        return id_EstacaoDePartida;
    }

    public void setId_EstacaoDePartida(long id_EstacaoDePartida) {
        this.id_EstacaoDePartida = id_EstacaoDePartida;
    }

    public long getId_EstacaoDeChegada() {
        return id_EstacaoDeChegada;
    }

    public void setId_EstacaoDeChegada(long id_EstacaoDeChegada) {
        this.id_EstacaoDeChegada = id_EstacaoDeChegada;
    }

    public long getId_Clima() {
        return id_Clima;
    }

    public void setId_Clima(long id_Clima) {
        this.id_Clima = id_Clima;
    }

    public long getId_Lotacao() {
        return id_Lotacao;
    }

    public void setId_Lotacao(long id_Lotacao) {
        this.id_Lotacao = id_Lotacao;
    }

    @Override
    public String toString() {
        return "TrajetoEntity [id_Trajeto=" + id_Trajeto + ", horarioPartida=" + horarioPartida + ", horarioChegada=" + horarioChegada + ", id_EstacaoDePartida=" + id_EstacaoDePartida + ", id_EstacaoDeChegada=" + id_EstacaoDeChegada + ", id_Clima=" + id_Clima + ", id_Lotacao=" + id_Lotacao + ", duracao=" + duracao + "]";
    }

    public long getId_UsuarioCadastrado() {
        return id_UsuarioCadastrado;
    }

    public void setId_UsuarioCadastrado(long id_UsuarioCadastrado) {
        this.id_UsuarioCadastrado = id_UsuarioCadastrado;
    }
}
