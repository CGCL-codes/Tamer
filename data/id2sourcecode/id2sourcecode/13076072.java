    private static int chopspace(byte[] buf, int len) {
        for (int i = 0; i < len; i++) {
            if ('A' <= buf[i] && buf[i] <= 'Z') {
                buf[i] = (byte) ('a' + buf[i] - 'A');
            } else if (buf[i] == ' ') {
                len--;
                for (int j = i; j < len; j++) {
                    buf[j] = buf[j + 1];
                }
                i--;
            }
        }
        return len;
    }
