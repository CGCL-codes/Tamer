    byte[] getRGB(byte[] data, int size) throws IOException {
        byte[] rgb = data;
        byte temp;
        int i;
        for (i = 0; i < size * 3; i += 3) {
            temp = rgb[i];
            rgb[i] = rgb[i + 2];
            rgb[i + 2] = temp;
        }
        return rgb;
    }
