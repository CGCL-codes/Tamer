    public void writeAll() {
        if (getReadOnly()) {
            log.error("unexpected write operation when readOnly is set");
        }
        setBusy(true);
        setToWrite(false);
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in write()");
        if ((_cvVector.elementAt(_row)).siVal() >= 0) {
            _progState = WRITING_PI4W;
        } else {
            _progState = WRITING_SI4W;
        }
        retries = 0;
        if (log.isDebugEnabled()) log.debug("invoke PI write for CV write");
        (_cvVector.elementAt(_row)).writePI(_status);
    }
