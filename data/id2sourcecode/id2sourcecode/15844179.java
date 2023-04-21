    public void newDecVariableValue(String name, int CV, String mask, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly) {
        setFileDirty(true);
        String comment = "";
        int minVal = 0;
        int maxVal = 255;
        _cvModel.addCV("" + CV, readOnly, infoOnly, writeOnly);
        int row = getRowCount();
        JButton bw = new JButton("Write");
        bw.setActionCommand("W" + row);
        bw.addActionListener(this);
        _writeButtons.addElement(bw);
        JButton br = new JButton("Read");
        br.setActionCommand("R" + row);
        br.addActionListener(this);
        _readButtons.addElement(br);
        VariableValue v = new DecVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, minVal, maxVal, _cvModel.allCvVector(), _status, null);
        rowVector.addElement(v);
        v.addPropertyChangeListener(this);
    }
