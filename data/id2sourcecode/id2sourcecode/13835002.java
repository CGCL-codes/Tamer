    public void writeAll() {
        if (log.isDebugEnabled()) log.debug("CV " + getCvNum() + "," + getSecondCvNum() + " write() invoked");
        if (getReadOnly()) log.error("CV " + getCvNum() + "," + getSecondCvNum() + " unexpected write operation when readOnly is set");
        setToWrite(false);
        setBusy(true);
        if (_progState != IDLE) log.warn("CV " + getCvNum() + "," + getSecondCvNum() + " Programming state " + _progState + ", not IDLE, in write()");
        _progState = WRITING_FIRST;
        if (log.isDebugEnabled()) log.debug("CV " + getCvNum() + "," + getSecondCvNum() + " invoke CV write");
        (_cvVector.elementAt(getCvNum())).write(_status);
    }
