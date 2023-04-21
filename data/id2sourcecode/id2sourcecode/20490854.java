    private char[] encodeToArray(byte[] ba) {
        boolean padDouble = false;
        boolean padSingle = false;
        int[] source = new int[3];
        int[] result = new int[4];
        int index = 0;
        char[] output = new char[((ba.length - 1) / 3 + 1) << 2];
        for (int i = 0; i < ba.length; i += 3) {
            source[0] = ba[i] & 0xff;
            if (i + 1 < ba.length) {
                source[1] = ba[i + 1] & 0xff;
            } else {
                padDouble = true;
                source[1] = 0;
            }
            if (i + 2 < ba.length) {
                source[2] = ba[i + 2] & 0xff;
            } else {
                padSingle = true;
                source[2] = 0;
            }
            split3to4(source, result);
            output[index++] = encodeSet[result[0]];
            output[index++] = encodeSet[result[1]];
            output[index++] = padDouble ? PADCHAR : encodeSet[result[2]];
            output[index++] = padSingle ? PADCHAR : encodeSet[result[3]];
        }
        return output;
    }
