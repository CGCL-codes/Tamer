    public boolean checkDocSignature(Certificate docSigningCert) throws GeneralSecurityException {
        byte[] eContent = getEContent();
        byte[] signature = getEncryptedDigest(signedData);
        String encAlg = getSignerInfo(signedData).getDigestEncryptionAlgorithm().getObjectId().getId();
        if (encAlg == null) {
            String digestAlg = getSignerInfo(signedData).getDigestAlgorithm().getObjectId().getId();
            MessageDigest digest = MessageDigest.getInstance(digestAlg);
            digest.update(eContent);
            byte[] digestBytes = digest.digest();
            return Arrays.equals(digestBytes, signature);
        }
        if (encAlg.equals(RSA_SA_PSS_OID.toString())) {
            encAlg = lookupMnemonicByOID(getSignerInfo(signedData).getDigestAlgorithm().getObjectId()) + "withRSA/PSS";
        }
        Signature sig = Signature.getInstance(encAlg);
        sig.initVerify(docSigningCert);
        sig.update(eContent);
        return sig.verify(signature);
    }
