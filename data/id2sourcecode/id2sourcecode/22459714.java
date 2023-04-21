    private static int getSHA1BasedIntFromString(String input) {
        byte[] bytes = null;
        try {
            bytes = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        byte[] value;
        synchronized (md) {
            value = md.digest(bytes);
        }
        int ret = 0;
        int i = 0;
        for (byte b : value) {
            ret ^= (b & 0xff) << (3 - i);
            if (++i > 3) i = 0;
        }
        return ret;
    }
