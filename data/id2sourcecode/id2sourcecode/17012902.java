    public static void swapOrder32(byte[] buffer, int byteOffset, int sampleCount) {
        int byteMax = sampleCount * 4 + byteOffset - 3;
        int i = byteOffset;
        while (i < byteMax) {
            byte h = buffer[i];
            buffer[i] = buffer[i + 3];
            buffer[i + 3] = h;
            i++;
            h = buffer[i];
            buffer[i] = buffer[++i];
            buffer[i++] = h;
            i++;
        }
    }
