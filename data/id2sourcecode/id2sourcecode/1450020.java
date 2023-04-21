    public void readAll() {
        setBusy(true);
        setToRead(false);
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in read()");
        if ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).siVal() >= 0) {
            _progState = WRITING_PI4R;
        } else {
            _progState = WRITING_SI4R;
        }
        retries = 0;
        if (log.isDebugEnabled()) log.debug("invoke PI write for CV read");
        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writePI(_status);
    }
