    public void writeTo(OutputStream out) throws IOException {
        int ch;
        while (-1 < (ch = this.read())) out.write(ch);
    }
