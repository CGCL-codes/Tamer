    public static DigestAlgAndValueType getDigestAlgAndValue(byte[] data, ObjectFactory xadesObjectFactory, be.fedict.eid.applet.service.signer.jaxb.xmldsig.ObjectFactory xmldsigObjectFactory, DigestAlgo digestAlgorithm) {
        DigestAlgAndValueType digestAlgAndValue = xadesObjectFactory.createDigestAlgAndValueType();
        DigestMethodType digestMethod = xmldsigObjectFactory.createDigestMethodType();
        digestAlgAndValue.setDigestMethod(digestMethod);
        digestMethod.setAlgorithm(digestAlgorithm.getXmlAlgoId());
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(digestAlgorithm.getAlgoId());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("message digest algo error: " + e.getMessage(), e);
        }
        byte[] digestValue = messageDigest.digest(data);
        digestAlgAndValue.setDigestValue(digestValue);
        return digestAlgAndValue;
    }
