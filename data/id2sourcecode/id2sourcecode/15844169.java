    protected VariableValue processIndexedPairVal(Element child, int row, boolean readOnly, boolean infoOnly, boolean writeOnly, String name, String comment, String cvName, boolean opsOnly, int cv, String mask, String item) throws NumberFormatException {
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
        int factor = 1;
        if ((a = child.getAttribute("factor")) != null) {
            factor = Integer.valueOf(a.getValue()).intValue();
        }
        int offset = 0;
        if ((a = child.getAttribute("offset")) != null) {
            offset = Integer.valueOf(a.getValue()).intValue();
        }
        String uppermask = "VVVVVVVV";
        if ((a = child.getAttribute("upperMask")) != null) {
            uppermask = a.getValue();
        }
        String highCVname = "";
        int highCVnumber = -1;
        int highCVpiVal = -1;
        int highCVsiVal = -1;
        if ((a = child.getAttribute("highCVname")) != null) {
            highCVname = a.getValue();
            int x = highCVname.indexOf('.');
            highCVnumber = Integer.valueOf(highCVname.substring(0, x)).intValue();
            int y = highCVname.indexOf('.', x + 1);
            if (y > 0) {
                highCVpiVal = Integer.valueOf(highCVname.substring(x + 1, y)).intValue();
                x = highCVname.lastIndexOf('.');
                highCVsiVal = Integer.valueOf(highCVname.substring(x + 1)).intValue();
            } else {
                x = highCVname.lastIndexOf('.');
                highCVpiVal = Integer.valueOf(highCVname.substring(x + 1)).intValue();
            }
        }
        int highCVrow = _indxCvModel.addIndxCV(row, highCVname, _piCv, highCVpiVal, _siCv, highCVsiVal, highCVnumber, readOnly, infoOnly, writeOnly);
        iv = new IndexedPairVariableValue(row, name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cv, mask, minVal, maxVal, _indxCvModel.allIndxCvVector(), _status, item, highCVrow, highCVname, factor, offset, uppermask);
        return iv;
    }
