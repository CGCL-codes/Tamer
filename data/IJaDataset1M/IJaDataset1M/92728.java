package der.ponto;

import java.math.BigDecimal;
import java.util.Date;

public class FrequenciaAdicionalNoturno {

    private String nome;

    private String lotacao;

    private Date dataEntrada;

    private Date dataSaida;

    private Date horaEntrada;

    private Date horaSaida;

    private Long quantidadeMinutos;

    private BigDecimal quantidadeSegundoAdicionalNoturno;

    public Long getQuantidadeMinutos() {
        return quantidadeMinutos;
    }

    public void setQuantidadeMinutos(Long quantidadeMinutos) {
        this.quantidadeMinutos = quantidadeMinutos;
    }

    public String getLotacao() {
        return lotacao;
    }

    public void setLotacao(String lotacao) {
        this.lotacao = lotacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(Date horaSaida) {
        this.horaSaida = horaSaida;
    }

    public BigDecimal getQuantidadeSegundoAdicionalNoturno() {
        return quantidadeSegundoAdicionalNoturno;
    }

    public void setQuantidadeSegundoAdicionalNoturno(BigDecimal quantidadeSegundoAdicionalNoturno) {
        this.quantidadeSegundoAdicionalNoturno = quantidadeSegundoAdicionalNoturno;
    }
}
