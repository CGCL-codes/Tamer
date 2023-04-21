    public void makeKey(byte[] entropy, byte[] key, int offset, int len) {
        try {
            MessageDigest ctx = MessageDigest.getInstance("SHA1");
            int ic = 0;
            while (len > 0) {
                ic++;
                for (int i = 0; i < ic; i++) {
                    ctx.update((byte) 0);
                }
                ctx.update(entropy, 0, entropy.length);
                int bc;
                if (len > 20) {
                    ctx.digest(key, offset, 20);
                    bc = 20;
                } else {
                    byte[] hash = ctx.digest();
                    bc = Math.min(len, hash.length);
                    System.arraycopy(hash, 0, key, offset, bc);
                }
                offset += bc;
                len -= bc;
            }
            wipe(entropy);
        } catch (Exception e) {
            throw new RuntimeException("Could not generate key: " + e.getMessage());
        }
    }
