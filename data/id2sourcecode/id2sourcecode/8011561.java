    public static byte[] getCertificateDigestValue(Certificate cert) throws CertDigestException {
        byte[] code;
        MessageDigest md;
        byte[] digestvalue;
        try {
            code = cert.getEncoded();
            md = MessageDigest.getInstance(DIGEST_ALGORITHM_NAME);
            md.update(code);
            digestvalue = md.digest(code);
        } catch (CertificateEncodingException e) {
            throw new CertDigestException("ERROR - Faild to calculate the encoded form of the certificate", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CertDigestException("ERROR - The digest algorithm " + DIGEST_ALGORITHM_NAME + " is not available", e);
        }
        return digestvalue;
    }
