    public SplitVariableValue(String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname, int pSecondCV, int pFactor, int pOffset, String uppermask) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JTextField("0", 5);
        _defaultColor = _value.getBackground();
        _value.setBackground(COLOR_UNKNOWN);
        mFactor = pFactor;
        mOffset = pOffset;
        _value.addActionListener(this);
        _value.addFocusListener(this);
        mSecondCV = pSecondCV;
        lowerbitmask = maskVal(mask);
        lowerbitoffset = offsetVal(mask);
        upperbitmask = maskVal(uppermask);
        upperbitoffset = offsetVal(uppermask);
        String t = mask;
        while (t.length() > 0) {
            if (!t.startsWith("V")) upperbitoffset++;
            t = t.substring(1);
        }
        if (log.isDebugEnabled()) log.debug("CV " + getCvNum() + "," + getSecondCvNum() + " upper mask " + uppermask + " had offsetVal=" + offsetVal(uppermask) + " so upperbitoffset=" + upperbitoffset);
        CvValue cv = (_cvVector.elementAt(getCvNum()));
        cv.addPropertyChangeListener(this);
        cv.setState(CvValue.FROMFILE);
        CvValue cv1 = (_cvVector.elementAt(getSecondCvNum()));
        cv1.addPropertyChangeListener(this);
        cv1.setState(CvValue.FROMFILE);
    }
