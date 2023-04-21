    public ArrayFile openW() throws FileNotFoundException, IOException {
        if (channel != null) throw new IOException("ArrayFile already open");
        channel = new FileOutputStream(name).getChannel();
        internalBuffer.clear();
        mode = Mode.WRITE;
        return this;
    }
