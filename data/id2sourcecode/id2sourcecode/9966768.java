    String getMD5(byte[] input, String JCEProvider) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
        MessageDigest hash = null;
        if (JCEProvider == null) {
            hash = MessageDigest.getInstance("MD5");
        } else {
            hash = MessageDigest.getInstance("MD5", JCEProvider);
        }
        byte[] digest = hash.digest(input);
        return new String(Base64.encode(digest));
    }
