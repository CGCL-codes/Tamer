    private static String getCertFingerPrint(String algorithm, Certificate certificate) throws CertificateEncodingException, NoSuchAlgorithmException {
        byte encoded[] = certificate.getEncoded();
        MessageDigest messagedigest = MessageDigest.getInstance(algorithm);
        byte digest[] = messagedigest.digest(encoded);
        return toHexString(digest);
    }
