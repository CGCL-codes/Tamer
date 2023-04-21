package es.devel.opentrats.model.DTO;

import java.util.Date;

/**
 *
 * @author pau
 */
public class LastAttentionDTO {

    private Long idInvoiceLine;

    private Date date;

    private Date time;

    private String details;

    private int session;

    private int intensity;

    private Long refCustomer;

    private String nameCustomer;

    private String surnameCustomer;

    private Integer refEmployee;

    private String nameEmployee;

    private String surnameEmployee;

    public LastAttentionDTO() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getIdInvoiceLine() {
        return idInvoiceLine;
    }

    public void setIdInvoiceLine(Long idInvoiceLine) {
        this.idInvoiceLine = idInvoiceLine;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public Long getRefCustomer() {
        return refCustomer;
    }

    public void setRefCustomer(Long refCustomer) {
        this.refCustomer = refCustomer;
    }

    public Integer getRefEmployee() {
        return refEmployee;
    }

    public void setRefEmployee(Integer refEmployee) {
        this.refEmployee = refEmployee;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public String getSurnameCustomer() {
        return surnameCustomer;
    }

    public void setSurnameCustomer(String surnameCustomer) {
        this.surnameCustomer = surnameCustomer;
    }

    public String getSurnameEmployee() {
        return surnameEmployee;
    }

    public void setSurnameEmployee(String surnameEmployee) {
        this.surnameEmployee = surnameEmployee;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
