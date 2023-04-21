    private String calculateA1(String username, String passwd) throws DigestAuthenticationException {
        if (realm == null) throw new DigestAuthenticationException("Realm can't be null");
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new DigestAuthenticationException(nsae.toString());
        }
        String input = username + ":" + realm + ":" + passwd;
        md.update(getEncodedBytes(input));
        return toHex(md.digest());
    }
