    void hashAddress(MessageDigest digest) {
        address = new int[(digest.getDigestLength() + 3) / 4];
        digest.update(iaddress.getAddress(), 0, 4);
        digest.update((byte) (port >>> 8));
        digest.update((byte) port);
        byte[] buffer = digest.digest();
        int off = 0;
        for (int ind = 0; ind < Constants.MAX_BITS / 32; ind++) {
            int value = buffer[off++] & 0xFF;
            value = (value << 8) | (buffer[off++] & 0xFF);
            value = (value << 8) | (buffer[off++] & 0xFF);
            value = (value << 8) | (buffer[off++] & 0xFF);
            address[ind] = value;
        }
    }
