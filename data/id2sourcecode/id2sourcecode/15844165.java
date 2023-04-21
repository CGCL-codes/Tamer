    protected VariableValue processDecVal(Element child, String name, String comment, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int CV, String mask, String item) throws NumberFormatException {
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
        v = new DecVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, minVal, maxVal, _cvModel.allCvVector(), _status, item);
        return v;
    }
