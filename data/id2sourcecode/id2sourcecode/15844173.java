    protected VariableValue processSpeedTableVal(Element child, int CV, boolean readOnly, boolean infoOnly, boolean writeOnly, String name, String comment, boolean opsOnly, String mask, String item) throws NumberFormatException {
        VariableValue v;
        Attribute a;
        int minVal = 0;
        int maxVal = 255;
        if ((a = child.getAttribute("min")) != null) {
            minVal = Integer.valueOf(a.getValue()).intValue();
        }
        if ((a = child.getAttribute("max")) != null) {
            maxVal = Integer.valueOf(a.getValue()).intValue();
        }
        Attribute entriesAttr = child.getAttribute("entries");
        int entries = 28;
        try {
            if (entriesAttr != null) {
                entries = entriesAttr.getIntValue();
            }
        } catch (org.jdom.DataConversionException e1) {
        }
        for (int i = 0; i < entries; i++) {
            _cvModel.addCV("" + (CV + i), readOnly, infoOnly, writeOnly);
        }
        v = new SpeedTableVarValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, minVal, maxVal, _cvModel.allCvVector(), _status, item, entries);
        return v;
    }
