    @Test
    public void testReadWriteRandom() throws IOException {
        for (int i = 0; i < 200; i += 3) {
            final byte[] buf = new byte[i * 100];
            VerySecureRandom.getInstance().nextBytes(buf);
            final BitInputStream aBIS = new BitInputStream(new NonBlockingByteArrayInputStream(buf), true);
            final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream();
            final BitOutputStream aBOS = new BitOutputStream(aBAOS, true);
            int nBitCount = buf.length * 8;
            while (nBitCount > 0) {
                final int nBits = Math.min(nBitCount, Math.max(1, VerySecureRandom.getInstance().nextInt(13)));
                aBOS.writeBits(aBIS.readBits(nBits), nBits);
                nBitCount -= nBits;
            }
            final byte[] read = aBAOS.toByteArray();
            assertArrayEquals(buf, read);
        }
    }
