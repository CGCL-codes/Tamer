    public static ID getSHA1BasedID(byte[] bytes, int sizeInByte) {
        byte[] value;
        synchronized (md) {
            value = md.digest(bytes);
        }
        if (sizeInByte > value.length) {
            throw new IllegalArgumentException("size is too large: " + sizeInByte + " > " + value.length);
        }
        return canonicalize(new ID(value, sizeInByte));
    }
