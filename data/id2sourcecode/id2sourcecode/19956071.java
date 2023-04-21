    public static byte[] getUTF16PrepairedBytes(byte[] data, int offset, int length) {
        if (data == null) return null;
        byte[] buffer = ByteReader.getBytes(data, offset, length);
        byte swapElement;
        if (buffer == null) return null;
        assert ((buffer.length & 1) == 0);
        for (int i = 0; i < buffer.length; i += 2) {
            swapElement = buffer[i];
            buffer[i] = buffer[i + 1];
            buffer[i + 1] = swapElement;
        }
        return buffer;
    }
