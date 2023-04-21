    public void run(String[] args) throws Throwable {
        int n = args.length - 1;
        if (n >= 0) {
            String[] args2 = new String[n];
            for (int i = 0; i < n; ++i) args2[i] = args[i + 1];
            run(args[0], args2);
        }
    }
