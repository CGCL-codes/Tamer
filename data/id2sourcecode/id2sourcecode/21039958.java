    public Object[] getScalarFuncParams(Object[] params) {
        if (Helper.hasNulls(params, 1)) return null;
        if (!XFunction.class.isInstance(params[1])) return null;
        XFunction func = (XFunction) params[1];
        Class classRet = func.getReturnType();
        if (!isScalarType(classRet) || func.getArgTypes().length != 1) {
            return null;
        }
        Object[] ret = new Object[params.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = params[i + 1];
        }
        return ret;
    }
