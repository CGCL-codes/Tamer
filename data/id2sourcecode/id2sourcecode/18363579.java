    private static byte[] getFingerprint(X509Certificate cert) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(DIGEST);
            md.update(cert.getEncoded());
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }
