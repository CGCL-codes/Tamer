    String getSHA256(byte[] input, String JCEProvider) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
        MessageDigest hash = null;
        if (JCEProvider == null) {
            hash = MessageDigest.getInstance("SHA-256");
        } else {
            hash = MessageDigest.getInstance("SHA-256", JCEProvider);
        }
        byte[] digest = hash.digest(input);
        return new String(Base64.encode(digest));
    }
