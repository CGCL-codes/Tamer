package org.ubispotsim.targetspot;

import org.ubispotsim.common.Constants;
import org.ubispotsim.landmark.Landmark;

/**
 * @author Hao Ji Signal information in a snapshot stemmed from a log file.
 */
public class Snapshot {

    private int no;

    private String begin, end;

    private int sn;

    private int tws;

    private String[] lm = { "?", "?" };

    private int[] sc = { 0, 0 };

    private String realElemId;

    Landmark sigInfo;

    public Snapshot() {
        no = 0;
        begin = end = "";
        sn = 0;
        tws = 0;
        realElemId = "?";
    }

    public Snapshot(Snapshot ss) {
        no = ss.getNo();
        begin = ss.getBegin();
        end = ss.getEnd();
        sn = ss.getSn();
        tws = ss.getTws();
        for (int i = 0; i < ss.getLm().length; i++) {
            lm[i] = ss.getLm()[i];
            sc[i] = ss.getSc()[i];
        }
        sigInfo = new Landmark(ss.getSigInfo());
    }

    public int getNo() {
        return no;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public int getSn() {
        return sn;
    }

    public int getTws() {
        return tws;
    }

    public String[] getLm() {
        return lm;
    }

    public int[] getSc() {
        return sc;
    }

    public String getRealElemId() {
        return realElemId;
    }

    public Landmark getSigInfo() {
        return sigInfo;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public void setTws(int tws) {
        this.tws = tws;
    }

    public void setLm(int index, String data) {
        lm[index] = data;
    }

    public void setSc(int index, int data) {
        sc[index] = data;
    }

    public void setRealElemId(String realElemId) {
        this.realElemId = realElemId;
    }

    public void setSigInfo(Landmark sigInfo) {
        this.sigInfo = new Landmark(sigInfo);
    }

    public String toString() {
        String str = "No: " + no + "  Begin: " + begin + "  End: " + end + Constants.NEWLINE;
        str += "SnTime: " + sn + "  TimeWSize: " + tws;
        str += sigInfo.toString() + Constants.NEWLINE;
        return str;
    }
}
