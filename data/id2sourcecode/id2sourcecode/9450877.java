    void executeWrite(VariableValue var) {
        setBusy(true);
        if (_programmingVar != null) log.error("listener already set at write start");
        _programmingVar = var;
        _read = false;
        _programmingVar.addPropertyChangeListener(this);
        if (justChanges) {
            _programmingVar.writeChanges();
        } else {
            _programmingVar.writeAll();
        }
    }
