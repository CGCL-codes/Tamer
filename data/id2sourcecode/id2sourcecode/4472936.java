    public void addCV(String s, boolean readOnly, boolean infoOnly, boolean writeOnly) {
        int num = Integer.valueOf(s).intValue();
        if (_cvAllVector.elementAt(num) == null) {
            CvValue cv = new CvValue(num, mProgrammer);
            cv.setReadOnly(readOnly);
            _cvAllVector.setElementAt(cv, num);
            _cvDisplayVector.addElement(cv);
            cv.addPropertyChangeListener(this);
            JButton bw = new JButton(rbt.getString("ButtonWrite"));
            _writeButtons.addElement(bw);
            JButton br = new JButton(rbt.getString("ButtonRead"));
            _readButtons.addElement(br);
            JButton bc = new JButton(rbt.getString("ButtonCompare"));
            _compareButtons.addElement(bc);
            if (infoOnly || readOnly) {
                if (writeOnly) {
                    bw.setEnabled(true);
                    bw.setActionCommand("W" + _numRows);
                    bw.addActionListener(this);
                } else {
                    bw.setEnabled(false);
                }
                if (infoOnly) {
                    br.setEnabled(false);
                    bc.setEnabled(false);
                } else {
                    br.setEnabled(true);
                    br.setActionCommand("R" + _numRows);
                    br.addActionListener(this);
                    bc.setEnabled(true);
                    bc.setActionCommand("C" + _numRows);
                    bc.addActionListener(this);
                }
            } else {
                bw.setEnabled(true);
                bw.setActionCommand("W" + _numRows);
                bw.addActionListener(this);
                if (writeOnly) {
                    br.setEnabled(false);
                    bc.setEnabled(false);
                } else {
                    br.setEnabled(true);
                    br.setActionCommand("R" + _numRows);
                    br.addActionListener(this);
                    bc.setEnabled(true);
                    bc.setActionCommand("C" + _numRows);
                    bc.addActionListener(this);
                }
            }
            _numRows++;
            fireTableDataChanged();
        }
        CvValue cv = _cvAllVector.elementAt(num);
        if (readOnly) cv.setReadOnly(readOnly);
        if (infoOnly) {
            cv.setReadOnly(infoOnly);
            cv.setInfoOnly(infoOnly);
        }
        if (writeOnly) cv.setWriteOnly(writeOnly);
    }
