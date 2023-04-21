    private String calculateA2(String method, String url) throws DigestAuthenticationException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new DigestAuthenticationException(nsae.toString());
        }
        String input = method + ":" + url;
        md.update(getEncodedBytes(input));
        return toHex(md.digest());
    }
