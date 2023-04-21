    public void index() throws IOException {
        outputStream("application/pdf").write(readResourceBytes("/test.pdf"));
    }
