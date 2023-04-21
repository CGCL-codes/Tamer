    private String getMD5Hash(String name) {
        byte[] md5bytes = md5.digest(name.getBytes());
        String md5String = new String();
        int upper;
        int lower;
        for (int i = 0; i < md5bytes.length; i++) {
            upper = (md5bytes[i] & (15 << 4)) >> 4;
            lower = md5bytes[i] & 15;
            md5String += Integer.toHexString(upper);
            md5String += Integer.toHexString(lower);
        }
        return md5String;
    }
