    public CompositeVariableValue(String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, minVal, maxVal, v, status, stdname);
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JComboBox();
        if (log.isDebugEnabled()) log.debug("New Composite named " + name);
    }
