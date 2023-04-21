    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(buf, reader, writer));
    }
