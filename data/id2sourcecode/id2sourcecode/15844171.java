    protected VariableValue processLongAddressVal(int CV, boolean readOnly, boolean infoOnly, boolean writeOnly, String name, String comment, boolean opsOnly, String mask, String item) {
        VariableValue v;
        int minVal = 0;
        int maxVal = 255;
        _cvModel.addCV("" + (CV + 1), readOnly, infoOnly, writeOnly);
        v = new LongAddrVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, minVal, maxVal, _cvModel.allCvVector(), _status, item);
        return v;
    }
