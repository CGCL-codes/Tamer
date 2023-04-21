    public Object call(Object[] args) throws Throwable {
        final Object[] nargs = new Object[args.length - 1];
        for (int i = 0; i < nargs.length; i++) {
            nargs[i] = args[i + 1];
        }
        try {
            return mtd.getMethod().invoke(args[0], nargs);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw Utils.wrapInvocationException(e);
        }
    }
