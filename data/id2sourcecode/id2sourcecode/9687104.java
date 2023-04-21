    private String getSubjectKeyIdentifier(X509Certificate certificate) {
        byte[] subjectKeyIdentifierData = certificate.getExtensionValue(X509Extensions.SubjectKeyIdentifier.getId());
        if (null == subjectKeyIdentifierData) {
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA1 error: " + e.getMessage());
            }
            String skidId = new String(Hex.encodeHex(messageDigest.digest(certificate.getPublicKey().getEncoded())));
            return skidId;
        }
        SubjectKeyIdentifierStructure subjectKeyIdentifierStructure;
        try {
            subjectKeyIdentifierStructure = new SubjectKeyIdentifierStructure(subjectKeyIdentifierData);
        } catch (IOException e) {
            throw new RuntimeException("SKI error: " + e.getMessage());
        }
        String skidId = new String(Hex.encodeHex(subjectKeyIdentifierStructure.getKeyIdentifier()));
        return skidId;
    }
