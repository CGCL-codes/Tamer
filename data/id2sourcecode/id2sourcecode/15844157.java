    public void setRow(int row, Element e) {
        String name = LocaleSelector.getAttribute(e, "label");
        if (log.isDebugEnabled()) log.debug("Starting to setRow \"" + name + "\"");
        String item = (e.getAttribute("item") != null ? e.getAttribute("item").getValue() : null);
        if (item == null) {
            item = e.getAttribute("label").getValue();
            log.debug("no item attribute for \"" + item + "\"");
        }
        if (name == null) {
            name = item;
            log.debug("no label attribute for \"" + item + "\"");
        }
        String comment = null;
        if (e.getAttribute("comment") != null) comment = e.getAttribute("comment").getValue();
        int CV = -1;
        if (e.getAttribute("CV") != null) CV = Integer.valueOf(e.getAttribute("CV").getValue()).intValue();
        String mask = null;
        if (e.getAttribute("mask") != null) mask = e.getAttribute("mask").getValue(); else {
            mask = "VVVVVVVV";
        }
        boolean readOnly = e.getAttribute("readOnly") != null ? e.getAttribute("readOnly").getValue().equals("yes") : false;
        boolean infoOnly = e.getAttribute("infoOnly") != null ? e.getAttribute("infoOnly").getValue().equals("yes") : false;
        boolean writeOnly = e.getAttribute("writeOnly") != null ? e.getAttribute("writeOnly").getValue().equals("yes") : false;
        boolean opsOnly = e.getAttribute("opsOnly") != null ? e.getAttribute("opsOnly").getValue().equals("yes") : false;
        if (_cvModel.getProgrammer() != null && !_cvModel.getProgrammer().getCanRead()) {
            if (readOnly) {
                readOnly = false;
                infoOnly = true;
            }
            if (!infoOnly) {
                writeOnly = true;
            }
        }
        JButton bw = new JButton("Write");
        _writeButtons.addElement(bw);
        JButton br = new JButton("Read");
        _readButtons.addElement(br);
        setButtonsReadWrite(readOnly, infoOnly, writeOnly, bw, br, row);
        if (_cvModel == null) {
            log.error("CvModel reference is null; cannot add variables");
            return;
        }
        if (CV > 0) _cvModel.addCV("" + CV, readOnly, infoOnly, writeOnly);
        Element child;
        VariableValue v = null;
        if ((child = e.getChild("decVal")) != null) {
            v = processDecVal(child, name, comment, readOnly, infoOnly, writeOnly, opsOnly, CV, mask, item);
        } else if ((child = e.getChild("hexVal")) != null) {
            v = processHexVal(child, name, comment, readOnly, infoOnly, writeOnly, opsOnly, CV, mask, item);
        } else if ((child = e.getChild("enumVal")) != null) {
            v = processEnumVal(child, name, comment, readOnly, infoOnly, writeOnly, opsOnly, CV, mask, item);
        } else if ((child = e.getChild("compositeVal")) != null) {
            v = processCompositeVal(child, name, comment, readOnly, infoOnly, writeOnly, opsOnly, CV, mask, item);
        } else if ((child = e.getChild("speedTableVal")) != null) {
            v = processSpeedTableVal(child, CV, readOnly, infoOnly, writeOnly, name, comment, opsOnly, mask, item);
        } else if ((child = e.getChild("longAddressVal")) != null) {
            v = processLongAddressVal(CV, readOnly, infoOnly, writeOnly, name, comment, opsOnly, mask, item);
        } else if ((child = e.getChild("shortAddressVal")) != null) {
            v = processShortAddressVal(name, comment, readOnly, infoOnly, writeOnly, opsOnly, CV, mask, item, child);
        } else if ((child = e.getChild("splitVal")) != null) {
            v = processSplitVal(child, CV, readOnly, infoOnly, writeOnly, name, comment, opsOnly, mask, item);
        } else {
            reportBogus();
            return;
        }
        processModifierElements(e, v);
        setToolTip(e, v);
        rowVector.addElement(v);
        v.setState(VariableValue.FROMFILE);
        v.addPropertyChangeListener(this);
        if (setDefaultValue(e, v)) {
            _cvModel.getCvByNumber(CV).setState(VariableValue.FROMFILE);
        }
    }
