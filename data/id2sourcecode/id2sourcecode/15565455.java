    @Test
    public void testWriteManyLowOrder() throws IOException {
        for (int i = 0; i < 200; i += 3) {
            final byte[] buf = new byte[i * 100];
            VerySecureRandom.getInstance().nextBytes(buf);
            final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream();
            final BitOutputStream aBOS = new BitOutputStream(aBAOS, false);
            for (final byte b : buf) aBOS.writeBits(b & 0xff, 8);
            final byte[] written = aBAOS.toByteArray();
            final BitInputStream aBIS = new BitInputStream(new NonBlockingByteArrayInputStream(written), false);
            aBAOS.reset();
            for (int x = 0; x < written.length; ++x) aBAOS.write(aBIS.readBits(8));
            final byte[] read = aBAOS.toByteArray();
            assertArrayEquals(buf, read);
        }
    }
