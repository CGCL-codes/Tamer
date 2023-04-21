    public void writeAllTo(OutputStream os) throws IOException {
        InputStream is = getInputStream();
        int b;
        while ((b = is.read()) > -1) os.write(b);
    }
