    public void write(InputStream is) throws IOException {
        byte ib[] = new byte[1024];
        int readed;
        while ((readed = is.read(ib)) != -1) write(ib, 0, readed);
    }
