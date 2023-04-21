    VariableValue makeVar(String label, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String item) {
        EnumVariableValue v1 = new EnumVariableValue(label, comment, "", readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, minVal, maxVal, v, status, item);
        v1.nItems(9);
        v1.addItem("0");
        v1.addItem("1");
        v1.addItem("2");
        v1.addItem("3");
        v1.addItem("4");
        v1.addItem("5");
        v1.addItem("6");
        v1.addItem("7");
        v1.addItem("9", 9);
        v1.lastItem();
        return v1;
    }
