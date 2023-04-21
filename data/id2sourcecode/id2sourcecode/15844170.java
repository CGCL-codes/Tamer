    protected VariableValue processIndexedVal(Element child, int row, String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cv, String mask, String item) throws NumberFormatException {
        VariableValue iv;
        int minVal = 0;
        int maxVal = 255;
        Attribute a;
        if ((a = child.getAttribute("min")) != null) {
            minVal = Integer.valueOf(a.getValue()).intValue();
        }
        if ((a = child.getAttribute("max")) != null) {
            maxVal = Integer.valueOf(a.getValue()).intValue();
        }
        iv = new IndexedVariableValue(row, name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cv, mask, minVal, maxVal, _indxCvModel.allIndxCvVector(), _status, item);
        return iv;
    }
