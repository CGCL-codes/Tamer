    private int refill() throws IOException {
        assert (pos >= length);
        pos = 0;
        length = 0;
        md.reset();
        while (length < buffer.length) {
            int r = in.read();
            if (r < 0) {
                break;
            }
            buffer[length++] = (byte) (r & 0xFF);
        }
        if (length == 0) {
            return -1;
        }
        int digestLength = md.getDigestLength();
        length -= digestLength;
        if (length <= 0) {
            throw new StreamCorruptedException("Illegal payload length: " + length);
        }
        md.update(buffer, 0, length);
        byte[] digest = md.digest();
        assert (digest.length == digestLength);
        for (int i = 0; i < digest.length; i++) {
            if (digest[i] != buffer[length + i]) {
                throw new StreamCorruptedException("Checksums do not match");
            }
        }
        return length;
    }
