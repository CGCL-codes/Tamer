    public boolean checkToken(String expirationTime, String uid, String type, String additionalParams, String tokenToCheck) throws NoSuchAlgorithmException {
        String sessionid = expirationTime + uid + type + additionalParams + salt;
        byte[] defaultBytes = sessionid.getBytes();
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String int_s = Integer.toHexString(0xFF & messageDigest[i]);
            if (int_s.length() > 1) {
                hexString.append(int_s);
            } else {
                hexString.append("0" + int_s);
            }
        }
        if (tokenToCheck.contentEquals(hexString)) {
            return true;
        } else {
            return false;
        }
    }
