    public byte[] sign() {
        byte[] output = digest.digest();
        digest.reset();
        digest.update(opad);
        return digest.digest(output);
    }
