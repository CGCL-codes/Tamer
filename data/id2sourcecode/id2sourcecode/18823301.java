    public static String md5(InputStream data) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        int b = -1;
        while ((b = data.read()) != -1) {
            digest.update((byte) b);
        }
        byte[] hash = digest.digest();
        return StringUtil.getHexString(hash);
    }
