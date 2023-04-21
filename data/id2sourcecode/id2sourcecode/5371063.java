    public static String digestBytesToString(byte[] b_value, String algorithm) throws Exception {
        MessageDigest algo = MessageDigest.getInstance(algorithm);
        algo.reset();
        algo.update(b_value);
        return StringTool.byteArrayToString(algo.digest());
    }
