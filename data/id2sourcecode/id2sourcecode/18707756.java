    VariableValue makeVar(String label, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String item) {
        CvValue cvNext = new CvValue(cvNum + offset, p);
        cvNext.setValue(0);
        v.setElementAt(cvNext, cvNum + offset);
        return new SplitVariableValue(label, comment, "", readOnly, infoOnly, writeOnly, opsOnly, cvNum, "XXXXVVVV", minVal, maxVal, v, status, item, cvNum + offset, 1, 0, "VVVVVVVV");
    }
