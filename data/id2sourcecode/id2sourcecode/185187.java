    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.err.println("MicroBench {<benchmark>|all} [<iterations>]");
            return;
        }
        String benchmarkName = args[0];
        if (args.length == 2) {
            iterations = Integer.parseInt(args[1]);
        }
        new ImplementsMyInterface();
        new Child1();
        new Child2();
        Method[] methods;
        if (!benchmarkName.equals("all")) {
            methods = new Method[1];
            try {
                methods[0] = MicroBench.class.getDeclaredMethod("bench" + benchmarkName, null);
            } catch (NoSuchMethodException e) {
                methods[0] = null;
            }
            if (methods[0] == null) {
                System.err.println("Benchmark \"" + benchmarkName + "\" not found.");
                return;
            }
        } else {
            methods = MicroBench.class.getDeclaredMethods();
        }
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (!m.getName().startsWith("bench", 0)) {
                continue;
            }
            sun.misc.JIT.compileMethod(m, false);
            try {
                System.out.println("Starting " + m.getName() + "...");
                long starttime = System.currentTimeMillis();
                for (int j = 0; j < iterations; j++) {
                    m.invoke(null, null);
                }
                long totaltime = System.currentTimeMillis() - starttime;
                System.out.println("	Time spent: " + totaltime + " milliseconds.");
            } catch (Throwable e) {
                e.printStackTrace();
                return;
            }
        }
    }
