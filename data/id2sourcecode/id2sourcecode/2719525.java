    public boolean checkDocSignature(Certificate docSigningCert) throws GeneralSecurityException {
        byte[] eContent = getEContent(signedData);
        byte[] signature = getEncryptedDigest(signedData);
        DERObjectIdentifier encAlgId = getSignerInfo(signedData).getDigestEncryptionAlgorithm().getObjectId();
        String encAlgJavaString = lookupMnemonicByOID(encAlgId);
        if (encAlgId.getId() == null) {
            String digestAlg = getSignerInfo(signedData).getDigestAlgorithm().getObjectId().getId();
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance(digestAlg);
            } catch (Exception e) {
                digest = MessageDigest.getInstance(digestAlg, PROVIDER);
            }
            digest.update(eContent);
            byte[] digestBytes = digest.digest();
            return Arrays.equals(digestBytes, signature);
        }
        if (encAlgId.equals(PKCS1_RSA_OID)) {
            encAlgJavaString = lookupMnemonicByOID(getSignerInfo(signedData).getDigestAlgorithm().getObjectId()) + "withRSA";
        }
        Signature sig = null;
        try {
            sig = Signature.getInstance(encAlgJavaString);
        } catch (Exception e) {
            sig = Signature.getInstance(encAlgJavaString, PROVIDER);
        }
        if (encAlgId.equals(PKCS1_RSA_PSS_OID)) {
            try {
                final DEREncodable parameters = getSignerInfo(signedData).getDigestEncryptionAlgorithm().getParameters();
                if (parameters != null) {
                    AlgorithmParameters params = AlgorithmParameters.getInstance("PSS");
                    params.init(parameters.getDERObject().getEncoded());
                    sig.setParameter(params.getParameterSpec(PSSParameterSpec.class));
                }
            } catch (IOException ex) {
                throw new GeneralSecurityException("Unable to parse algorithm parameters", ex);
            }
        }
        sig.initVerify(docSigningCert);
        sig.update(eContent);
        return sig.verify(signature);
    }
