    public byte[] getSKIBytesFromCert(X509Certificate cert) throws WSSecurityException {
        byte[] derEncodedValue = cert.getExtensionValue(SKI_OID);
        if (cert.getVersion() < 3 || derEncodedValue == null) {
            PublicKey key = cert.getPublicKey();
            if (!(key instanceof RSAPublicKey)) {
                throw new WSSecurityException(1, "noSKIHandling", new Object[] { "Support for RSA key only" });
            }
            byte[] encoded = key.getEncoded();
            byte[] value = new byte[encoded.length - 22];
            System.arraycopy(encoded, 22, value, 0, value.length);
            MessageDigest sha;
            try {
                sha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException ex) {
                throw new WSSecurityException(1, "noSKIHandling", new Object[] { "Wrong certificate version (<3) and no SHA1 message digest availabe" });
            }
            sha.reset();
            sha.update(value);
            return sha.digest();
        }
        byte abyte0[] = new byte[derEncodedValue.length - 4];
        System.arraycopy(derEncodedValue, 4, abyte0, 0, abyte0.length);
        return abyte0;
    }
