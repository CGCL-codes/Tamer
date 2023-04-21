    public static void copyStreams(InputStream in, OutputStream out) throws IOException {
        int b;
        while ((b = in.read()) != -1) out.write(b);
        out.flush();
        out.close();
    }
