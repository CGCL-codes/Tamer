    String getSHA384(byte[] input, String JCEProvider) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
        MessageDigest hash = null;
        if (JCEProvider == null) {
            hash = MessageDigest.getInstance("SHA-384");
        } else {
            hash = MessageDigest.getInstance("SHA-384", JCEProvider);
        }
        byte[] digest = hash.digest(input);
        return new String(Base64.encode(digest));
    }
