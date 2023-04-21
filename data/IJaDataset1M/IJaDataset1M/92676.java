package menfis.model.bean;

import java.io.Serializable;

/**
 * Classe de Chave Prim�ria CalendarizacaoPK para classe de entidade Calendarizacao
 * 
 * @author Luiz Eduardo
 */
public class CalendarizacaoPK implements Serializable {

    private int mes;

    private int ano;

    private int centroCusto;

    /** Creates a new instance of CalendarizacaoPK */
    public CalendarizacaoPK() {
    }

    /**
     * Cria uma nova inst�ncia de CalendarizacaoPK com os valores especificados.
     * @param centroCusto o centroCusto do CalendarizacaoPK
     * @param ano o ano do CalendarizacaoPK
     * @param mes o mes do CalendarizacaoPK
     */
    public CalendarizacaoPK(int centroCusto, int ano, int mes) {
        this.centroCusto = centroCusto;
        this.ano = ano;
        this.mes = mes;
    }

    /**
     * Define o mes deste CalendarizacaoPK.
     * @return o mes
     */
    public int getMes() {
        return this.mes;
    }

    /**
     * Define o mes deste CalendarizacaoPK para o valor especificado.
     * @param mes o novo mes
     */
    public void setMes(int mes) {
        this.mes = mes;
    }

    /**
     * Define o ano deste CalendarizacaoPK.
     * @return o ano
     */
    public int getAno() {
        return this.ano;
    }

    /**
     * Define o ano deste CalendarizacaoPK para o valor especificado.
     * @param ano o novo ano
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * Define o centroCusto deste CalendarizacaoPK.
     * @return o centroCusto
     */
    public int getCentroCusto() {
        return this.centroCusto;
    }

    /**
     * Define o centroCusto deste CalendarizacaoPK para o valor especificado.
     * @param centroCusto o novo centroCusto
     */
    public void setCentroCusto(int centroCusto) {
        this.centroCusto = centroCusto;
    }

    /**
     * Retorna um valor de c�digo hash para o objeto.  Esta implementa��o computa
     * um valor de c�digo hash baseado nos campos id deste objeto.
     * @return um valor de c�digo hash para este objeto.
     */
    public int hashCode() {
        int hash = 0;
        hash += (int) centroCusto;
        hash += (int) ano;
        hash += (int) mes;
        return hash;
    }

    /**
     * Determina se outro objeto � igual a este CalendarizacaoPK.  O resultado �
     * <code>true</code> se e somente se o argumento n�o for nulo e for um objeto CalendarizacaoPK o qual
     * tem o mesmo valor para o campo id como este objeto.
     * @param object o objeto de refer�ncia com o qual comparar
     * @return <code>true</code> se este objeto � o mesmo como o argumento;
     * <code>false</code> caso contr�rio.
     */
    public boolean equals(Object object) {
        if (!(object instanceof CalendarizacaoPK)) {
            return false;
        }
        CalendarizacaoPK other = (CalendarizacaoPK) object;
        if (this.centroCusto != other.centroCusto) return false;
        if (this.ano != other.ano) return false;
        if (this.mes != other.mes) return false;
        return true;
    }

    /**
     * Retorna uma representa��o literal deste objeto.  Esta implementa��o cria
     * uma representa��o baseada nos campos id.
     * @return uma representa��o literal deste objeto.
     */
    public String toString() {
        return "objects.CalendarizacaoPK[centroCusto=" + centroCusto + ", ano=" + ano + ", mes=" + mes + "]";
    }
}
