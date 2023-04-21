    private static byte[] hexFrom(final char[] chars, int start, int len) {
        int size = (len + 1) / 2;
        final byte[] b = new byte[size];
        for (int i = 0; i < size; i++) {
            int n = 0;
            char ch1 = Character.toLowerCase(chars[start + i * 2]);
            char ch2 = Character.toLowerCase(chars[start + i * 2 + 1]);
            int flag = 0;
            for (int j = 0; j < hex.length; j++) {
                if (hex[j] == ch1) {
                    n += j * 0x10;
                    flag++;
                }
                if (hex[j] == ch2) {
                    n += j;
                    flag++;
                }
                if (flag == 2) break;
            }
            b[i] = (byte) n;
        }
        return b;
    }
