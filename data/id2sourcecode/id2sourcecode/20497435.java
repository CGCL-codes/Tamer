    @Override
    public int read() throws IOException {
        int readInt = super.read();
        sw.write(readInt);
        return readInt;
    }
