    public String getValue(String string) {
        StringBuffer hexString = new StringBuffer();
        md5.reset();
        md5.update(string.getBytes());
        byte[] result = md5.digest();
        for (int i = 0; i < result.length; i++) {
            hexString.append(Integer.toHexString((result[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3));
        }
        return hexString.toString();
    }
