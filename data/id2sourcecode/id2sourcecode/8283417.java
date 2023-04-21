    byte[] hexStringToBytes(String initialValue) {
        if (initialValue == null) {
            return null;
        }
        int size = initialValue.length();
        int limit = (size + 1) / 2;
        byte[] result = new byte[limit];
        if (size % 2 != 0) {
            result[--limit] = hexCharToByte(initialValue.charAt(size - 1));
        }
        for (int i = 0, j = 0; i < limit; ++i) {
            byte high = hexCharToByte(initialValue.charAt(j++));
            byte low = hexCharToByte(initialValue.charAt(j++));
            result[i] = (byte) (high << 4 | low);
        }
        return result;
    }
