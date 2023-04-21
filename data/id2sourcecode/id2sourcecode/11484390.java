    public byte[] getMd5Hash() {
        md.reset();
        for (byte[] ba : window) {
            md.update(ba);
        }
        return md.digest();
    }
