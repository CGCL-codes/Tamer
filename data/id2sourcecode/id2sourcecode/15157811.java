    public ArrayFile openR() throws FileNotFoundException, IOException {
        if (channel != null) throw new IOException("ArrayFile already open");
        channel = new FileInputStream(name).getChannel();
        mode = Mode.READ;
        return this;
    }
