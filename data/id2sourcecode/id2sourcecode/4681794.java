    public void readAllFrom(InputStream is) throws IOException {
        OutputStream os = getOutputStream();
        int b;
        while ((b = is.read()) > -1) os.write(b);
    }
