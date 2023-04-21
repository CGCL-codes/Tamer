    public IndexedPairVariableValue(int row, String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname, int secondCVrow, String pSecondCV, int pFactor, int pOffset, String uppermask) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        _row = row;
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JTextField("0", 3);
        _secondCVrow = secondCVrow;
        _secondCV = pSecondCV;
        _Factor = pFactor;
        _Offset = pOffset;
        lowerbitmask = maskVal(mask);
        lowerbitoffset = offsetVal(mask);
        upperbitmask = maskVal(uppermask);
        upperbitoffset = offsetVal(uppermask);
        CvValue cv = (_cvVector.elementAt(_row));
        cv.addPropertyChangeListener(this);
        cv.setState(CvValue.FROMFILE);
        CvValue cv1 = (_cvVector.elementAt(_secondCVrow));
        cv1.addPropertyChangeListener(this);
        cv1.setState(CvValue.FROMFILE);
    }
