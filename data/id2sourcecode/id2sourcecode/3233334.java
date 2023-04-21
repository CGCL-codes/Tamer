    public String getAliasForX509CertThumb(byte[] thumb) throws WSSecurityException {
        Certificate cert = null;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            throw new WSSecurityException(0, "noSHA1availabe");
        }
        try {
            for (Enumeration e = keystore.aliases(); e.hasMoreElements(); ) {
                String alias = (String) e.nextElement();
                Certificate[] certs = keystore.getCertificateChain(alias);
                if (certs == null || certs.length == 0) {
                    cert = keystore.getCertificate(alias);
                    if (cert == null) {
                        return null;
                    }
                } else {
                    cert = certs[0];
                }
                if (!(cert instanceof X509Certificate)) {
                    continue;
                }
                sha.reset();
                try {
                    sha.update(cert.getEncoded());
                } catch (CertificateEncodingException e1) {
                    throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE, "encodeError");
                }
                byte[] data = sha.digest();
                if (Arrays.equals(data, thumb)) {
                    return alias;
                }
            }
        } catch (KeyStoreException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "keystore");
        }
        return null;
    }
