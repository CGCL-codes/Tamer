    public Object add(Object objNew) {
        Object objOld = objary[0];
        for (int i = 0; i < (intLength - 1); i++) {
            objary[i] = objary[i + 1];
        }
        objary[intLength - 1] = objNew;
        return objOld;
    }
