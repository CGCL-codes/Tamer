    private static Object[] asTestMethodArgs(final Object[] args) {
        if (1 == args.length) {
            return null;
        } else {
            final Object[] testMethodArgs = new Object[args.length - 1];
            for (int i = 0; i < testMethodArgs.length; ++i) {
                testMethodArgs[i] = args[i + 1];
            }
            return testMethodArgs;
        }
    }
