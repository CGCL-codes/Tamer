    public LongAddrVariableValue(String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JTextField("0", 5);
        _defaultColor = _value.getBackground();
        _value.setBackground(COLOR_UNKNOWN);
        _value.addActionListener(this);
        _value.addFocusListener(this);
        CvValue cv = (_cvVector.elementAt(getCvNum()));
        cv.addPropertyChangeListener(this);
        cv.setState(CvValue.FROMFILE);
        CvValue cv1 = (_cvVector.elementAt(getCvNum() + 1));
        cv1.addPropertyChangeListener(this);
        cv1.setState(CvValue.FROMFILE);
    }
