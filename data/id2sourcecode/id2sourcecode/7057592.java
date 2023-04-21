    public void writeAll() {
        setToWrite(false);
        if (getReadOnly()) log.error("unexpected write operation when readOnly is set");
        setBusy(true);
        _cvVector.elementAt(getCvNum()).write(_status);
    }
