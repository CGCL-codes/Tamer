    public static final String md5Checksum(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes;
            try {
                bytes = value.getBytes("UTF-8");
            } catch (UnsupportedEncodingException uee) {
                bytes = value.getBytes();
            }
            StringBuilder result = new StringBuilder();
            for (byte b : md.digest(bytes)) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
