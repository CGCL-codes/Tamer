    private int toNumericRepresentationHelper(String perm) {
        int read = perm.substring(0, 1).equals("-") ? 0 : 1;
        int write = perm.substring(1, 2).equals("-") ? 0 : 1;
        int exec = perm.substring(2, 3).equals("-") ? 0 : 1;
        return (read * 4) + (write * 2) + exec;
    }
