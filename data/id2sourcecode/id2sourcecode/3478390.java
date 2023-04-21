    private byte[] getBytes(int length) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(SEED_BYTES);
        byte[] bytes = new byte[length];
        byte[] sourceBytes = md.digest();
        for (int i = 0; i < length; i++) {
            bytes[i] = sourceBytes[i % sourceBytes.length];
        }
        return bytes;
    }
