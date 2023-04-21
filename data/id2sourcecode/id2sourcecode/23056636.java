    public byte[] digest() {
        MessageDigest tmp;
        try {
            tmp = (MessageDigest) state.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("MD5 not clonable in this JRE\n");
        }
        return tmp.digest();
    }
