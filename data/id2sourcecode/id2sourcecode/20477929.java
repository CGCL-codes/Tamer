    @Override
    public String encode(String name) {
        String encoded = "";
        byte[] defaultBytes = name.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            encoded += hexString;
        } catch (NoSuchAlgorithmException nsae) {
        }
        return encoded;
    }
