    public static synchronized String getMD5forString(String tmp) {
        MessageDigest digest = md;
        if (tmp == null || tmp == "") tmp = "<empty>";
        digest.reset();
        digest.update(tmp.getBytes());
        byte[] hash = digest.digest();
        String dig = new String(hash);
        return dig;
    }
