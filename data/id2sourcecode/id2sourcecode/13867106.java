    VariableValue makeVar(String label, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String item) {
        CvValue cvNext = new CvValue(cvNum + 1, p);
        cvNext.setValue(0);
        v.setElementAt(cvNext, cvNum + 1);
        return new CompositeVariableValue(label, comment, "", readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, minVal, maxVal, v, status, item);
    }
