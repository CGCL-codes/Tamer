    @Test
    public void roundtripPositive() {
        EbmlEncoder encoder = new EbmlEncoder();
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        long write = 0x1122334455667788L;
        for (int i = 8; i >= 0; i--) {
            buffer.clear();
            encoder.encodeUnsignedInteger(buffer, write, i);
            buffer.flip();
            long read = decoder.decodeUnsignedInteger(buffer, i);
            assertThat(read, isEqualTo(write));
            write >>= 8;
        }
    }
