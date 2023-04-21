package com.release.bean;

public class DBServerBean {

    private int dataServerID = 0;

    private String dataServer = "";

    private String iP = "";

    private String nYShortPath = "";

    private String lNShortPath = "";

    private String refreshFreq = "";

    private String remarks = "";

    private String dBsRefreshed = "";

    private String dataServerHost = "";

    public String getDataServer() {
        return dataServer;
    }

    public void setDataServer(String dataServer) {
        this.dataServer = dataServer;
    }

    public String getIP() {
        return iP;
    }

    public void setIP(String ip) {
        iP = ip;
    }

    public String getLNShortPath() {
        return lNShortPath;
    }

    public void setLNShortPath(String shortPath) {
        lNShortPath = shortPath;
    }

    public String getNYShortPath() {
        return nYShortPath;
    }

    public void setNYShortPath(String shortPath) {
        nYShortPath = shortPath;
    }

    public String getRefreshFreq() {
        return refreshFreq;
    }

    public void setRefreshFreq(String refreshFreq) {
        this.refreshFreq = refreshFreq;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getDataServerID() {
        return dataServerID;
    }

    public void setDataServerID(int dataServerID) {
        this.dataServerID = dataServerID;
    }

    public String getDataServerHost() {
        return dataServerHost;
    }

    public void setDataServerHost(String dataServerHost) {
        this.dataServerHost = dataServerHost;
    }

    public String getDBsRefreshed() {
        return dBsRefreshed;
    }

    public void setDBsRefreshed(String bsRefreshed) {
        dBsRefreshed = bsRefreshed;
    }
}
