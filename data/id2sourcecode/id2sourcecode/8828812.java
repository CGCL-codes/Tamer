    public SpeedTableVarValue(String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname, int entries) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        nValues = entries;
        _min = minVal;
        _max = maxVal;
        _range = maxVal - minVal;
        models = new BoundedRangeModel[nValues];
        for (int i = 0; i < nValues; i++) {
            DefaultBoundedRangeModel j = new DefaultBoundedRangeModel(_range * i / (nValues - 1) + _min, 0, _min, _max);
            models[i] = j;
            CvValue c = _cvVector.elementAt(getCvNum() + i);
            c.setValue(_range * i / (nValues - 1) + _min);
            c.addPropertyChangeListener(this);
            c.setState(CvValue.FROMFILE);
        }
        _defaultColor = (new JSlider()).getBackground();
    }
