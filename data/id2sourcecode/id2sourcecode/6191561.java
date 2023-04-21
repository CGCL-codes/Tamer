    public synchronized String getHash(String s) {
        byte[] data = s.getBytes();
        this.messageDigest.update(data);
        return toHexString(this.messageDigest.digest());
    }
