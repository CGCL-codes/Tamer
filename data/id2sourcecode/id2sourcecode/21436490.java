    public void readFrom(InputStream in) throws IOException {
        int ch;
        while (-1 < (ch = in.read())) this.write(ch);
    }
