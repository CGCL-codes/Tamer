    public byte[] getFingerprintOfCertificate(String p_algorithm) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance(p_algorithm);
        md.update(m_objCertificate.getEncoded());
        return md.digest();
    }
