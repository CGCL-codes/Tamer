    protected void write(InputStream in, OutputStream out) throws java.io.IOException {
        int x;
        while ((x = in.read()) != -1) out.write(x);
        out.flush();
    }
