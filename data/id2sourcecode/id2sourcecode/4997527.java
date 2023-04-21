    public byte[] digest() {
        byte[] h1 = md.digest();
        md.update(key, 0, key.length);
        md.update(pad2, 0, pad2.length);
        md.update(h1, 0, h1.length);
        byte[] result = md.digest();
        reset();
        return result;
    }
