    public final byte[] digest() {
        if (digest != null) return digest;
        if (!EOF) try {
            while (read() != -1) {
            }
        } catch (IOException e) {
        }
        digest = md.digest();
        return digest;
    }
