    public String convertToMD5(String password) {
        String encodedPassword = "";
        byte[] defaultBytes = password.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            encodedPassword = hexString + "";
        } catch (NoSuchAlgorithmException nsae) {
            log.error("ERROR in encoding password. Please verify.");
            nsae.printStackTrace();
        }
        return encodedPassword;
    }
