    public void close() throws IOException {
        byte[] hash = digest.digest();
        char[] hex = new char[2 * hash.length];
        for (int i = 0; i < hash.length; i++) {
            hex[2 * i] = HEX[hash[i] / 16];
            hex[2 * i + 1] = HEX[hash[i] % 16];
        }
        contentHash = new String(hex);
    }
