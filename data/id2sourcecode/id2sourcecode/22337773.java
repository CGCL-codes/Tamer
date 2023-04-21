    public void write(InputStream input) throws IOException {
        for (int ch = input.read(); ch != -1; ch = input.read()) write(ch);
        input.close();
    }
