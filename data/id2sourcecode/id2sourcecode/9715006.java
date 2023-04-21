    static String hash(final String msg) {
        MessageDigest _md = md5.get();
        String hash = "hash";
        if (_md != null) {
            _md.reset();
            byte[] buf = _md.digest(msg.getBytes());
            char[] c = new char[buf.length * 2];
            for (int i = 0; i < buf.length; i++) {
                c[i * 2] = hex[(buf[i] & 0xf0) >> 4];
                c[(i * 2) + 1] = hex[buf[i] & 0x0f];
            }
            hash = new String(c);
        }
        return hash;
    }
