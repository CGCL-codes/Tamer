    public static synchronized String generateMD5(String input) {
        byte[] defaultBytes = input.getBytes();
        try {
            if (algorithm == null) {
                algorithm = MessageDigest.getInstance("MD5");
            }
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
    }
