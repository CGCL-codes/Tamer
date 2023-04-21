    public static void readTo(InputStream in, OutputStream out, String codeSet) throws IOException {
        InputStreamReader in_reader = new InputStreamReader(in, codeSet);
        OutputStreamWriter out_writer = new OutputStreamWriter(out, codeSet);
        readTo(in_reader, out_writer);
    }
