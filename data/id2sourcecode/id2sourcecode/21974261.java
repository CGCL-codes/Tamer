    public ByteArray hashWithSHA1() {
        byte[] hashed;
        synchronized (md) {
            hashed = md.digest(this.barray);
        }
        return new ByteArray(hashed);
    }
