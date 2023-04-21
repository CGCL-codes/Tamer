    private void reopen() throws IOException {
        channel = raf.getChannel();
        bb = channel.map(MapMode.READ_WRITE, 0, fileLength);
        lb = bb.asLongBuffer();
    }
