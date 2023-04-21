    protected final byte[] engineDoFinal() {
        byte[] hmac;
        try {
            inner.digest(innerHash, 0, hashLength);
            outer.update(innerHash, 0, hashLength);
            outer.digest(outerHash, 0, hashLength);
        } catch (DigestException e) {
            throw new Error("Error/bug in HMAC, buffer too short");
        }
        hmac = new byte[macLength];
        System.arraycopy(outerHash, 0, hmac, 0, macLength);
        updatePads();
        return hmac;
    }
