    public static void printStream(OkiInputStream in) throws FilingException {
        byte buf[] = new byte[128];
        int len;
        while ((len = in.read(buf)) != -1) System.out.write(buf, 0, len);
    }
