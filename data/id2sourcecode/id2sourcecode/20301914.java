    protected static String DN2MD5Filename(X509Certificate cert) {
        byte[] issuer = cert.getIssuerDN().getName().getBytes();
        byte[] user = cert.getSubjectDN().getName().getBytes();
        String token = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(issuer);
            md5.update(user);
            token = toHex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm " + e.getMessage(), e);
        }
        return token;
    }
