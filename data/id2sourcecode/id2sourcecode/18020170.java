    public static String getMD5String(byte[] array) {
        m_digest.update(array, 0, array.length);
        BigInteger md5 = new BigInteger(1, m_digest.digest());
        String md5Str = md5.toString(16);
        return md5Str;
    }
