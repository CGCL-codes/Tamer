    private boolean checkAKI(X509Certificate subject, X509Certificate issuer, ActionOutput output) {
        boolean retVal = true;
        byte[] publicKey = issuer.getPublicKey().getEncoded();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(publicKey);
            try {
                ASN1OctetString string = (ASN1OctetString) DERUtil.getDERObject(subject.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId()));
                AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier((ASN1Sequence) DERUtil.getDERObject(string.getOctets()));
                if (!MessageDigest.isEqual(digest.digest(), aki.getKeyIdentifier())) {
                    output.setSuccess(false);
                    output.setLevel(PKIBRWarningLevel.WARNING_LEVEL_CRITICAL_ERROR);
                    output.setMessage(Bundle.getInstance().getResourceString(this, "PKIBR_RULE_INVALID_AKI_WARN"));
                    retVal = false;
                }
            } catch (Exception e) {
                output.setSuccess(false);
                output.setLevel(PKIBRWarningLevel.WARNING_LEVEL_ERROR);
                output.setMessage(Bundle.getInstance().getResourceString(this, "PKIBR_RULE_INVALID_CHAIN_WARN"));
                retVal = false;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new PKIBRValidatorException(Bundle.getInstance().getResourceString(this, "PKIBR_CRYPTO_PROVIDER_ERROR"), e);
        }
        return retVal;
    }
