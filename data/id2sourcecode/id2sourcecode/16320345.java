    public static void checkReference(X509CRL crl, CompleteRevocationRefsType completeRevocationRefs) throws XAdESValidationException {
        byte[] encodedCRL;
        try {
            encodedCRL = crl.getEncoded();
        } catch (CRLException e) {
            throw new XAdESValidationException("CRL encoding error: " + e.getMessage(), e);
        }
        CRLRefsType crlRefs = completeRevocationRefs.getCRLRefs();
        if (null == crlRefs) {
            throw new XAdESValidationException("missing CRLRefs");
        }
        for (CRLRefType crlRef : crlRefs.getCRLRef()) {
            DigestAlgAndValueType digestAlgAndValue = crlRef.getDigestAlgAndValue();
            String xmlDigestAlgo = digestAlgAndValue.getDigestMethod().getAlgorithm();
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance(getDigestAlgo(xmlDigestAlgo));
            } catch (NoSuchAlgorithmException e) {
                throw new XAdESValidationException("message digest algo error: " + e.getMessage(), e);
            }
            byte[] expectedDigestValue = messageDigest.digest(encodedCRL);
            byte[] refDigestValue = digestAlgAndValue.getDigestValue();
            if (Arrays.equals(expectedDigestValue, refDigestValue)) {
                return;
            }
        }
        throw new XAdESValidationException("CRL not referenced");
    }
