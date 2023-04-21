    private String calculateResponse(String a1s, String a2s) throws DigestAuthenticationException {
        if (nonce == null) {
            throw new DigestAuthenticationException("Can't have a null nonce value");
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new DigestAuthenticationException(nsae.toString());
        }
        String nString = a1s + ":" + nonce + ":" + a2s;
        md.update(getEncodedBytes(nString));
        return toHex(md.digest());
    }
