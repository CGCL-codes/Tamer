    public static byte[] encrypt(byte[] m, byte[] key) {
        byte[] iv = MD5.digest(Long.toString((new Random()).nextLong()));
        byte[] c = (new MD5OTP(key, iv)).update(m);
        return cat(iv, c);
    }
