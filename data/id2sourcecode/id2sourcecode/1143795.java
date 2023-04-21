    public static final byte[] decode(String s) {
        int i = s.length(), j = i, k;
        byte b;
        boolean lo = true;
        byte[] d;
        String S = s.toUpperCase();
        while (i-- > 0) if (value(S.charAt(i)) < 0) j--;
        i = s.length();
        if ((j & 1) == 1) {
            k = (j + 1) / 2;
            d = new byte[k];
        } else {
            k = j / 2;
            d = new byte[k];
        }
        while (i-- > 0) if ((b = value(S.charAt(i))) >= 0) {
            if (lo) {
                d[--k] = b;
                lo = false;
            } else {
                d[k] |= b << 4;
                lo = true;
            }
        }
        return d;
    }
