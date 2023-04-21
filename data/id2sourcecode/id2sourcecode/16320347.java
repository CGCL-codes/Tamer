    public static void checkReference(X509Certificate certificate, CompleteCertificateRefsType completeCertificateRefs) throws XAdESValidationException {
        byte[] encodedCert;
        try {
            encodedCert = certificate.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new XAdESValidationException("X509 encoding error: " + e.getMessage(), e);
        }
        CertIDListType certIDList = completeCertificateRefs.getCertRefs();
        if (null == certIDList) {
            throw new XAdESValidationException("missing CertRefs");
        }
        for (CertIDType certID : certIDList.getCert()) {
            DigestAlgAndValueType digestAlgAndValue = certID.getCertDigest();
            String xmlDigestAlgo = digestAlgAndValue.getDigestMethod().getAlgorithm();
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance(getDigestAlgo(xmlDigestAlgo));
            } catch (NoSuchAlgorithmException e) {
                throw new XAdESValidationException("message digest algo error: " + e.getMessage(), e);
            }
            byte[] expectedDigestValue = messageDigest.digest(encodedCert);
            byte[] refDigestValue = digestAlgAndValue.getDigestValue();
            if (Arrays.equals(expectedDigestValue, refDigestValue)) {
                return;
            }
        }
        throw new XAdESValidationException("X509 certificate not referenced");
    }
