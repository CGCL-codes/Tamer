    public void writeChanges() {
        if (getReadOnly()) log.error("unexpected writeChanges operation when readOnly is set");
        setBusy(true);
        updateCvForAddrChange();
        _cvVector.elementAt(getCvNum()).write(_status);
    }
