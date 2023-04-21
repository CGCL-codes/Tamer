    protected VariableValue processSplitVal(Element child, int CV, boolean readOnly, boolean infoOnly, boolean writeOnly, String name, String comment, boolean opsOnly, String mask, String item) throws NumberFormatException {
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
        int highCV = CV + 1;
        if ((a = child.getAttribute("highCV")) != null) {
            highCV = Integer.valueOf(a.getValue()).intValue();
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
        _cvModel.addCV("" + (highCV), readOnly, infoOnly, writeOnly);
        v = new SplitVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, minVal, maxVal, _cvModel.allCvVector(), _status, item, highCV, factor, offset, uppermask);
        return v;
    }
