    public List<Object[]> getAllCombinations(Object[] parms) {
        List<Object[]> children = null;
        if (parms.length > 1) {
            Object[] cp = new Object[parms.length - 1];
            for (int i = 0; i < parms.length - 1; i++) {
                cp[i] = parms[i + 1];
            }
            children = getAllCombinations(cp);
        }
        List<Object[]> ret = new ArrayList<Object[]>();
        if (parms.length == 0) {
            ret.add(parms);
        } else {
            if (parms[0].getClass().isArray()) {
                for (Object o : (Object[]) parms[0]) {
                    process(ret, children, o);
                }
            } else {
                process(ret, children, parms[0]);
            }
        }
        return ret;
    }
