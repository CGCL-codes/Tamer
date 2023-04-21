    private String getMD5(String string) throws Error {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5 = digest.digest(string.getBytes());
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < md5.length; ++i) {
                buffer.append(Integer.toHexString((md5[i] >> 4) & 0x0F));
                buffer.append(Integer.toHexString(md5[i] & 0x0F));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new Error("No MD5 message digest found");
        }
    }
