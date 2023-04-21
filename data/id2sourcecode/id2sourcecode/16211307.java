    public void writeAll() {
        if (getReadOnly()) log.error("unexpected writeAll operation when readOnly is set");
        setBusy(true);
        updateCvForAddrChange();
        _cvVector.elementAt(getCvNum()).write(_status);
    }
