    public void indexedRead() {
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in read()");
        if ((_indxCvDisplayVector.elementAt(_row)).siVal() >= 0) {
            _progState = WRITING_PI4R;
        } else {
            _progState = WRITING_SI4R;
        }
        retries = 0;
        if (log.isDebugEnabled()) log.debug("invoke PI write for CV read");
        (_indxCvDisplayVector.elementAt(_row)).writePI(_status);
    }
