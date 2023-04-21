    public VariableValue(String label, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, Vector<CvValue> v, JLabel status, String item) {
        _label = label;
        _comment = comment;
        _cvName = cvName;
        _readOnly = readOnly;
        _infoOnly = infoOnly;
        _writeOnly = writeOnly;
        _opsOnly = opsOnly;
        _cvNum = cvNum;
        _mask = mask;
        _cvVector = v;
        _status = status;
        _item = item;
    }
