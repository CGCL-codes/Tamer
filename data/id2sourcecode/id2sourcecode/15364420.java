    public int addIndxCV(int row, String cvName, int piCv, int piVal, int siCv, int siVal, int iCv, boolean readOnly, boolean infoOnly, boolean writeOnly) {
        int existingRow = getCvByName(cvName);
        if (existingRow == -1) {
            row = _numRows++;
            CvValue indxCv = new CvValue(row, cvName, piCv, piVal, siCv, siVal, iCv, mProgrammer);
            indxCv.setReadOnly(readOnly);
            indxCv.setInfoOnly(infoOnly);
            _indxCvAllVector.setElementAt(indxCv, row);
            _indxCvDisplayVector.addElement(indxCv);
            indxCv.addPropertyChangeListener(this);
            JButton bw = new JButton("Write");
            _indxWriteButtons.addElement(bw);
            JButton br = new JButton("Read");
            _indxReadButtons.addElement(br);
            JButton bc = new JButton("Compare");
            _indxCompareButtons.addElement(bc);
            if (infoOnly || readOnly) {
                if (writeOnly) {
                    bw.setEnabled(true);
                    bw.setActionCommand("W" + row);
                    bw.addActionListener(this);
                } else {
                    bw.setEnabled(false);
                }
                if (infoOnly) {
                    br.setEnabled(false);
                    bc.setEnabled(false);
                } else {
                    br.setEnabled(true);
                    br.setActionCommand("R" + row);
                    br.addActionListener(this);
                    bc.setEnabled(true);
                    bc.setActionCommand("C" + row);
                    bc.addActionListener(this);
                }
            } else {
                bw.setEnabled(true);
                bw.setActionCommand("W" + row);
                bw.addActionListener(this);
                if (writeOnly) {
                    br.setEnabled(false);
                    bc.setEnabled(false);
                } else {
                    br.setEnabled(true);
                    br.setActionCommand("R" + row);
                    br.addActionListener(this);
                    bc.setEnabled(true);
                    bc.setActionCommand("C" + row);
                    bc.addActionListener(this);
                }
            }
            if (log.isDebugEnabled()) log.debug("addIndxCV adds row at " + row);
            fireTableDataChanged();
        } else {
            if (log.isDebugEnabled()) log.debug("addIndxCV finds existing row of " + existingRow + " with numRows " + _numRows);
            row = existingRow;
        }
        if (row > -1 && row < _indxCvAllVector.size()) {
            CvValue indxcv = _indxCvAllVector.elementAt(row);
            if (readOnly) indxcv.setReadOnly(readOnly);
            if (infoOnly) {
                indxcv.setReadOnly(infoOnly);
                indxcv.setInfoOnly(infoOnly);
            }
            if (writeOnly) indxcv.setWriteOnly(writeOnly);
        }
        return row;
    }
