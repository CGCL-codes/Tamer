    public static String byteToString(byte[] b, boolean compressedBytes) {
        String str = "";
        try {
            if (compressedBytes) {
                for (int i = 0; i < b.length; i += 2) {
                    byte[] b2 = new byte[2];
                    b2[0] = b[i];
                    b2[1] = b[i + 1];
                    int iChar = MTFToolClass.byteToShort(b2);
                    str += (char) iChar;
                }
            } else {
                for (int i = 0; i < b.length; i++) {
                    str += (char) b[i];
                }
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            str = "";
        }
        return str;
    }
