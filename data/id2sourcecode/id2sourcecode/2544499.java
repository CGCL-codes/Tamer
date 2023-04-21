    private static short[] convertBuffer(final byte[] buf) throws IOException {
        assertTrue(buf.length % 2 == 0);
        byte[] bigEndianBuf = new byte[buf.length];
        for (int i = 0; i < buf.length - 1; i += 2) {
            bigEndianBuf[i] = buf[i + 1];
            bigEndianBuf[i + 1] = buf[i];
        }
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bigEndianBuf));
        short[] samples = new short[buf.length / 2];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = dis.readShort();
        }
        return samples;
    }
