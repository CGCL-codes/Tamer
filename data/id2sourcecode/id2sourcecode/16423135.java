    public void read(long startPosn, byte[] data) throws IOException {
        FileInputStream stream = new FileInputStream(this.timFile);
        ByteBuffer bb = stream.getChannel().map(MapMode.READ_ONLY, (long) header.getHeaderLength() + startPosn, (long) data.length);
        bb.order(ByteOrder.nativeOrder());
        bb.get(data);
        stream.close();
    }
