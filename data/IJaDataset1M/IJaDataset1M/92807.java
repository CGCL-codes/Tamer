package com.aelitis.azureus.plugins.extseed;

public class ExternalSeedException extends Exception {

    private boolean permanent = false;

    public ExternalSeedException(String str) {
        super(str);
    }

    public ExternalSeedException(String str, Throwable e) {
        super(str, e);
    }

    public void setPermanentFailure(boolean b) {
        permanent = b;
    }

    public boolean isPermanentFailure() {
        return (permanent);
    }
}
