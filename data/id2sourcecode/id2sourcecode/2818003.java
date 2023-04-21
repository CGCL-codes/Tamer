    public static String getMD5FileName(String pass) {
        String result;
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            byte[] data = pass.getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            result = String.format("%1$032X", i);
        } catch (NoSuchAlgorithmException e) {
            _log.error(e.getMessage());
            result = Utils.GetUUID();
        }
        return (result);
    }
