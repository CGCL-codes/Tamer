    static void copy(InputStream is, OutputStream os) throws IOException {
        int by;
        while ((by = is.read()) >= 0) os.write(by);
        is.close();
        os.close();
    }
