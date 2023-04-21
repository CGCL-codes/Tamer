    private long hash(String str) {
        byte[] hash;
        long tmp = 0L;
        long value = 0L;
        m_md5.update(UTF8.encode(str));
        hash = m_md5.digest();
        tmp = hash[0] & 0xff;
        value |= tmp << (6 * 8);
        tmp = hash[1] & 0xff;
        value |= tmp << (5 * 8);
        tmp = hash[2] & 0xff;
        value |= tmp << (4 * 8);
        tmp = hash[3] & 0xff;
        value |= tmp << (3 * 8);
        tmp = hash[4] & 0xff;
        value |= tmp << (2 * 8);
        tmp = hash[5] & 0xff;
        value |= tmp << (1 * 8);
        tmp = hash[6] & 0xff;
        value |= tmp;
        return (value);
    }
