    private float readfloat(InputStream s) {
        byte[] buffer = new byte[4];
        try {
            s.read(buffer, 0, buffer.length);
        } catch (IOException e) {
            IJ.write("error reading float" + e);
            return (0);
        }
        int tmp = (int) (((buffer[0] & 0xff) << 24) | ((buffer[1] & 0xff) << 16) | ((buffer[2] & 0xff) << 8) | (buffer[3] & 0xff));
        return Float.intBitsToFloat(tmp);
    }
