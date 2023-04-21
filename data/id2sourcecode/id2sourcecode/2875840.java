    public void writeAll() {
        if (log.isDebugEnabled()) log.debug("write() invoked");
        if (getReadOnly()) log.error("unexpected write operation when readOnly is set");
        setToWrite(false);
        setBusy(true);
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in write()");
        _progState = WRITING_FIRST;
        if (log.isDebugEnabled()) log.debug("invoke CV write");
        (_cvVector.elementAt(getCvNum())).write(_status);
    }
