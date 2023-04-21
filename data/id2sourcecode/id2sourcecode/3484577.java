    public IndexedVariableValue(int row, String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        if (log.isDebugEnabled()) log.debug("ctor with cvName " + cvName + ", cvNum " + cvNum);
        _row = row;
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JTextField("0", 3);
        _defaultColor = _value.getBackground();
        CvValue cv = (_cvVector.elementAt(_row));
        if (log.isDebugEnabled()) log.debug("cv found as " + cv);
        cv.addPropertyChangeListener(this);
        if (cv.getInfoOnly()) {
            cv.setState(CvValue.READ);
        } else {
            cv.setState(CvValue.FROMFILE);
        }
    }
