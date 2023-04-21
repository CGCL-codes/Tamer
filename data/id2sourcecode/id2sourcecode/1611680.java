    public static String[] subArgs(String[] args) {
        String[] ret = new String[args.length - 1];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = args[i + 1];
        }
        return ret;
    }
