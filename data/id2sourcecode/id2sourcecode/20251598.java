    @Test(dataProvider = "doublePrecisionData")
    public void roundtripSinglePrecision(double value) {
        EbmlEncoder encoder = new EbmlEncoder();
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        float write = (float) value;
        encoder.encodeFloatingPoint(buffer, write, 4);
        buffer.flip();
        float read = (float) decoder.decodeFloatingPoint(buffer, 4);
        assertThat(read, isSameAs(write));
    }
