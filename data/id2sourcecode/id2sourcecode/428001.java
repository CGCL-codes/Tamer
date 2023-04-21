    private void readError() throws IOException {
        int c;
        InputStream in = proc.getErrorStream();
        while ((c = in.read()) != -1) System.err.write(c);
        in.close();
    }
