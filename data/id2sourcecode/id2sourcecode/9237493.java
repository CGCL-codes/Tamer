    byte[] getRGBA(byte[] data, int size) throws IOException {
        byte[] rgba = data;
        byte temp;
        int i;
        int numOfBytes = size * 4;
        for (i = 0; i < numOfBytes; i += 4) {
            temp = rgba[i];
            rgba[i] = rgba[i + 2];
            rgba[i + 2] = temp;
        }
        return rgba;
    }
