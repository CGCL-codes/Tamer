    public static void checkReference(OCSPResp ocspResp, CompleteRevocationRefsType completeRevocationRefs) throws XAdESValidationException {
        byte[] encodedOcsp;
        try {
            encodedOcsp = ocspResp.getEncoded();
        } catch (IOException e) {
            throw new XAdESValidationException("OCSP encoding error: " + e.getMessage(), e);
        }
        OCSPRefsType ocspRefs = completeRevocationRefs.getOCSPRefs();
        if (null == ocspRefs) {
            throw new XAdESValidationException("missing OCSPRefs");
        }
        for (OCSPRefType ocspRef : ocspRefs.getOCSPRef()) {
            DigestAlgAndValueType digestAlgAndValue = ocspRef.getDigestAlgAndValue();
            if (null == digestAlgAndValue) {
                continue;
            }
            String xmlDigestAlgo = digestAlgAndValue.getDigestMethod().getAlgorithm();
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance(getDigestAlgo(xmlDigestAlgo));
            } catch (NoSuchAlgorithmException e) {
                throw new XAdESValidationException("message digest algo error: " + e.getMessage(), e);
            }
            byte[] expectedDigestValue = messageDigest.digest(encodedOcsp);
            byte[] refDigestValue = digestAlgAndValue.getDigestValue();
            if (Arrays.equals(expectedDigestValue, refDigestValue)) {
                return;
            }
        }
        throw new XAdESValidationException("OCSP response not referenced");
    }
