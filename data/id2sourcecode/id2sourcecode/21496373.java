    public static IDLittleEndian getSHA1BasedID(byte[] message, int sizeInByte) {
        byte[] value;
        synchronized (md) {
            value = md.digest(message);
        }
        if (sizeInByte > value.length) {
            throw new IllegalArgumentException("size is too large: " + sizeInByte + " > " + value.length);
        }
        return canonicalize(new IDLittleEndian(value, sizeInByte));
    }
