    public static void copyStream(OkiInputStream in, OkiOutputStream out) throws FilingException {
        int b;
        while ((b = in.read()) != -1) out.write(b);
        out.close();
    }
