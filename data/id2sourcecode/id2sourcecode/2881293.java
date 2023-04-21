    public String getData() throws IOException {
        Reader is = this.getReader();
        StringWriter bos = new StringWriter();
        char chars[] = new char[200];
        int readCount = 0;
        while ((readCount = is.read(chars)) > 0) {
            bos.write(chars, 0, readCount);
        }
        is.close();
        return bos.toString();
    }
