    public static String md5_32(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(value.getBytes("UTF-8"));
            StringBuffer md5hash = new StringBuffer(digest.length * 2);
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(digest[i] & 0xFF);
                if (hex.length() == 1) md5hash.append('0');
                md5hash.append(hex);
            }
            return md5hash.toString();
        } catch (Exception e) {
            throw new OntopiaRuntimeException(e);
        }
    }
