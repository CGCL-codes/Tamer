    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (args.length < 1) throw new RuntimeException("Usage: java " + TimedRun.class.getCanonicalName() + " ClassName [args]");
        final ClassLoader cl = TimedRun.class.getClassLoader();
        final Class<?> target = cl.loadClass(args[0]);
        final Method runMe = target.getMethod("main", String[].class);
        final String[] otherArgs = new String[args.length - 1];
        for (int i = 0; i < otherArgs.length; i++) otherArgs[i] = args[i + 1];
        final long now = Calendar.getInstance().getTimeInMillis();
        runMe.invoke(null, new Object[] { otherArgs });
        final long delta = Calendar.getInstance().getTimeInMillis() - now;
        System.out.println("Run time : " + formatTime(delta));
    }
