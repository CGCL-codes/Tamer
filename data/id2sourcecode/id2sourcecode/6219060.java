    public static String get_hash(String content) {
        String hash = null;
        String md5val = "";
        MessageDigest algorithm = null;
        StringBuffer hexString = null;
        byte messageDigest[] = null;
        byte[] content_bytes = content.getBytes();
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            hash = "";
        }
        if (algorithm != null) {
            hexString = new StringBuffer();
            algorithm.reset();
            algorithm.update(content_bytes);
            messageDigest = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            hash = hexString.toString();
        }
        return hash;
    }
