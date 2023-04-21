    private static String[] getJobArgs(String[] args) {
        String[] sa = null;
        if (args.length > 1) {
            sa = new String[args.length - 1];
            for (int i = 0; i < sa.length; i++) {
                sa[i] = args[i + 1];
            }
        }
        return sa;
    }
