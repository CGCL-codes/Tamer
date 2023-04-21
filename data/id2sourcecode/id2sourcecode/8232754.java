    public String encrypt(String toEnc) {
        this.mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
        return new BigInteger(1, this.mdEnc.digest()).toString(16);
    }
