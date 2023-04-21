    public static Object[] getArgsWithVarArgs(Method m, Object[] args) {
        Class<?>[] params = m.getParameterTypes();
        int nParams = params.length;
        boolean hasVarArgs = nParams > 0 && params[nParams - 1].isArray();
        if (!hasVarArgs) return args;
        Object[] ret = new Object[params.length];
        System.arraycopy(args, 0, ret, 0, nParams - 1);
        int nVarArgs = args.length - (nParams - 1);
        Object varArgs = Array.newInstance(params[nParams - 1].getComponentType(), nVarArgs);
        for (int i = 0; i < nVarArgs; i++) {
            Array.set(varArgs, i, args[nParams - 1 + i]);
        }
        ret[nParams - 1] = varArgs;
        return ret;
    }
