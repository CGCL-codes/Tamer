    public void write(OutputStream output) throws IOException {
        for (int ch = read(); ch != -1; ch = read()) output.write(ch);
        close();
    }
