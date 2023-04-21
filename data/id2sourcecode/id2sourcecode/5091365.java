    private ByteString createRandom() {
        final MessageDigest d = Digests.sha1();
        final byte[] bytes = new byte[2048];
        for (int i = 0; i < 1500; i++) {
            R.nextBytes(bytes);
            d.update(bytes);
        }
        return ByteString.copyFrom(d.digest());
    }
