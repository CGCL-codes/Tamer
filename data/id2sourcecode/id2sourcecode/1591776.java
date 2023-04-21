    public byte[] grayByteArray2RGBByteArray(final byte[] gray_arr) {
        final byte[] rgbarr = new byte[gray_arr.length * 3];
        int valgray;
        for (int i = 0; i < gray_arr.length * 3; i += 3) {
            valgray = gray_arr[i / 3];
            rgbarr[i] = rgbarr[i + 1] = rgbarr[i + 2] = (byte) valgray;
        }
        return rgbarr;
    }
