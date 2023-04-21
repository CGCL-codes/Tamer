    public void read(long startPosn, float[] data) throws IOException {
        FileInputStream stream = new FileInputStream(this.timFile);
        ByteBuffer bb = stream.getChannel().map(MapMode.READ_ONLY, (long) header.getHeaderLength() + startPosn * 4L, (long) data.length * 4L);
        bb.order(ByteOrder.nativeOrder());
        fb = bb.asFloatBuffer();
        fb.get(data);
        stream.close();
    }
