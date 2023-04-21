    public static String code(String s) {
        byte[] content = s.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(content);
            byte digest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            int digestLength = digest.length;
            for (int i = 0; i < digestLength; i++) {
                hexString.append(hexDigit(digest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
