    public static String md5Sum(byte[] input, int limit) {
        try {
            if (md == null) md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] digest;
            if (limit == -1) {
                digest = md.digest(input);
            } else {
                md.update(input, 0, limit > input.length ? input.length : limit);
                digest = md.digest();
            }
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(hexDigit(digest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
