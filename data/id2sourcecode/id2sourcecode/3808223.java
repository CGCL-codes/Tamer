    public static String getHash(String value, String type) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance(type);
            byte d[] = sha1.digest(value.getBytes());
            StringBuilder buff = new StringBuilder();
            for (int i = 0; i < d.length; i++) buff.append(addLZ(Integer.toHexString(d[i] & 0xFF)));
            return buff.toString();
        } catch (Exception e) {
        }
        return "";
    }
