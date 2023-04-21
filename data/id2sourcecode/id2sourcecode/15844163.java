    public int setIndxRow(int row, Element e, String productID) {
        if (DecoderFile.isIncluded(e, productID) == false) {
            if (log.isDebugEnabled()) log.debug("include not match, return row - 1 =" + (row - 1));
            return row - 1;
        }
        String name = LocaleSelector.getAttribute(e, "label");
        if (log.isDebugEnabled()) log.debug("Starting to setIndexedRow \"" + name + "\"");
        String cvName = e.getAttributeValue("CVname");
        String item = (e.getAttribute("item") != null ? e.getAttribute("item").getValue() : null);
        String comment = null;
        if (e.getAttribute("comment") != null) comment = e.getAttribute("comment").getValue();
        int piVal = Integer.valueOf(e.getAttribute("PI").getValue()).intValue();
        int siVal = (e.getAttribute("SI") != null ? Integer.valueOf(e.getAttribute("SI").getValue()).intValue() : -1);
        int cv = Integer.valueOf(e.getAttribute("CV").getValue()).intValue();
        String mask = null;
        if (e.getAttribute("mask") != null) mask = e.getAttribute("mask").getValue(); else {
            mask = "VVVVVVVV";
        }
        boolean readOnly = e.getAttribute("readOnly") != null ? e.getAttribute("readOnly").getValue().equals("yes") : false;
        boolean infoOnly = e.getAttribute("infoOnly") != null ? e.getAttribute("infoOnly").getValue().equals("yes") : false;
        boolean writeOnly = e.getAttribute("writeOnly") != null ? e.getAttribute("writeOnly").getValue().equals("yes") : false;
        boolean opsOnly = e.getAttribute("opsOnly") != null ? e.getAttribute("opsOnly").getValue().equals("yes") : false;
        JButton br = new JButton("Read");
        _readButtons.addElement(br);
        JButton bw = new JButton("Write");
        _writeButtons.addElement(bw);
        setButtonsReadWrite(readOnly, infoOnly, writeOnly, bw, br, row);
        if (_indxCvModel == null) {
            log.error("IndexedCvModel reference is null; can not add variables");
            return -1;
        }
        int _newRow = _indxCvModel.addIndxCV(row, cvName, _piCv, piVal, _siCv, siVal, cv, readOnly, infoOnly, writeOnly);
        if (_newRow != row) {
            row = _newRow;
            if (log.isDebugEnabled()) log.debug("new row is " + _newRow + ", row was " + row);
        }
        VariableValue iv;
        iv = createIndexedVariableFromElement(e, row, name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cv, mask, item, productID);
        if (iv == null) {
            reportBogus();
            return -1;
        }
        processModifierElements(e, iv);
        setToolTip(e, iv);
        rowVector.addElement(iv);
        iv.setState(VariableValue.FROMFILE);
        iv.addPropertyChangeListener(this);
        Attribute a;
        if ((a = e.getAttribute("default")) != null) {
            String val = a.getValue();
            if (log.isDebugEnabled()) log.debug("Found default value: " + val + " for " + name);
            iv.setIntValue(Integer.valueOf(val).intValue());
            if (_indxCvModel.getCvByRow(row).getInfoOnly()) {
                _indxCvModel.getCvByRow(row).setState(VariableValue.READ);
            } else {
                _indxCvModel.getCvByRow(row).setState(VariableValue.FROMFILE);
            }
        } else {
            _indxCvModel.getCvByRow(row).setState(VariableValue.UNKNOWN);
        }
        return row;
    }
