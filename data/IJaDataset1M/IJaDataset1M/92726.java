package com.koossery.adempiere.fe.actions.accounting.gl.glFund;

import java.util.ArrayList;
import java.util.Map;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseForm;
import com.koossery.adempiere.fe.beans.accounting.gl.gLFund.GLFundBean;

/**
 * @version 1.0
 * @created 25-ao�t-2008 15:11:36
 */
public class GLFundForm extends KTAdempiereBaseForm {

    private GLFundBean glFundbean;

    private ArrayList listOfClientAllowed;

    public ArrayList listOfOrgAllowed;

    private int idGLFundSelected;

    private int idClientSelected;

    private int idOrgSelected;

    private Map glFundMap;

    private Map glFundMap1;

    public ArrayList listOfSchema;

    private String nomOrg;

    private String nomClient;

    private boolean active;

    private int display;

    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GLFundForm() {
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    public GLFundBean getGlFundbean() {
        return glFundbean;
    }

    public void setGlFundbean(GLFundBean glFundbean) {
        this.glFundbean = glFundbean;
    }

    public ArrayList getListOfClientAllowed() {
        return listOfClientAllowed;
    }

    public void setListOfClientAllowed(ArrayList listOfClientAllowed) {
        this.listOfClientAllowed = listOfClientAllowed;
    }

    public ArrayList getListOfOrgAllowed() {
        return listOfOrgAllowed;
    }

    public void setListOfOrgAllowed(ArrayList listOfOrgAllowed) {
        this.listOfOrgAllowed = listOfOrgAllowed;
    }

    public int getIdGLFundSelected() {
        return idGLFundSelected;
    }

    public void setIdGLFundSelected(int idGLFundSelected) {
        this.idGLFundSelected = idGLFundSelected;
    }

    public int getIdClientSelected() {
        return idClientSelected;
    }

    public void setIdClientSelected(int idClientSelected) {
        this.idClientSelected = idClientSelected;
    }

    public int getIdOrgSelected() {
        return idOrgSelected;
    }

    public void setIdOrgSelected(int idOrgSelected) {
        this.idOrgSelected = idOrgSelected;
    }

    public Map getGlFundMap() {
        return glFundMap;
    }

    public void setGlFundMap(Map glFundMap) {
        this.glFundMap = glFundMap;
    }

    public void resetForm() {
        setGlFundbean(new GLFundBean());
        if (getGlFundMap() != null) getGlFundMap().clear();
        setIdGLFundSelected(0);
    }

    public Map getGlFundMap1() {
        return glFundMap1;
    }

    public void setGlFundMap1(Map glFundMap1) {
        this.glFundMap1 = glFundMap1;
    }

    public ArrayList getListOfSchema() {
        return listOfSchema;
    }

    public void setListOfSchema(ArrayList listOfSchema) {
        this.listOfSchema = listOfSchema;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
