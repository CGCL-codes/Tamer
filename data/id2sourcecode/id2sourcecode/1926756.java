    private void makeCryptoKey(InputStream in, long plainLength) throws IOException {
        byte[] buf = new byte[Core.blockSize];
        Digest ctx = SHA1.getInstance();
        while (plainLength > 0) {
            int i = in.read(buf, 0, plainLength < buf.length ? (int) plainLength : buf.length);
            if (i > 0) {
                ctx.update(buf, 0, i);
                plainLength -= i;
            } else if (i == -1) throw new EOFException();
        }
        byte[] entropy = ctx.digest();
        cryptoKey = new byte[cipher.getKeySize() >> 3];
        Util.makeKey(entropy, cryptoKey, 0, cryptoKey.length);
    }
