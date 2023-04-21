    private static String getMD5(String key) {
        algorithm.reset();
        algorithm.update(key.getBytes());
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        return hexString.toString();
    }
