    public static String getMD5(byte[] fileContents) throws Exception {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(fileContents);
        byte messageDigest[] = algorithm.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            sb.append(String.format("%02x", 0xFF & messageDigest[i]));
        }
        return sb.toString();
    }
