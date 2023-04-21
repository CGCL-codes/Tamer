    public Object[] getBooleanFuncParams(Object[] params, int nScalarInd) {
        if (Helper.hasNulls(params, 1)) return null;
        if (!XFunction.class.isInstance(params[1])) return null;
        XFunction func = (XFunction) params[1];
        if (!func.getReturnType().equals(Boolean.class)) return null;
        Object[] ret = new Object[params.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = params[i + 1];
        }
        if (nScalarInd != -1 && nScalarInd != 0 && Vector.class.isInstance(params[nScalarInd])) {
            Vector cv = (Vector) params[nScalarInd];
            if (cv.size() == 0 || cv.elementAt(0) == null) return null;
            ret[nScalarInd - 1] = cv.elementAt(0);
        }
        return ret;
    }
