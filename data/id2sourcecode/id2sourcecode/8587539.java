    public static String hashIssuer(X509Certificate certificate) {
        String c_hash = new String("00000000");
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] derEncodedIssuer = certificate.getIssuerX500Principal().getEncoded();
            md5.update(derEncodedIssuer);
            byte[] messageDigest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 3; i >= 0; i--) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            c_hash = hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("No MD5 message digest algorithm available?", nsae);
        }
        return c_hash;
    }
