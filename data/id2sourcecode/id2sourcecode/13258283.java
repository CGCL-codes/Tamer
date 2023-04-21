    private static BinParameter[] getNewParameters(BinParameter[] old, int idx) {
        if ((old == null) || (old.length < 2) || (idx < 0) || (idx > old.length - 1)) {
            return new BinParameter[0];
        }
        BinParameter[] newParams = new BinParameter[old.length - 1];
        for (int i = 0; i < idx; i++) {
            newParams[i] = old[i];
        }
        for (int i = idx; i < old.length - 1; i++) {
            newParams[i] = old[i + 1];
        }
        return newParams;
    }
