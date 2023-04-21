    protected byte[] engineSign() throws SignatureException {
        if (privateKey instanceof PoorPrivateKey) {
            byte[] privateKeyAliasBytes = privateKey.toString().getBytes();
            if (privateKeyAliasBytes == null) throw new SignatureException("Alias for the signature is null");
            byte[] signedDataB = null;
            switch(signatureType) {
                case PoorSettings.SIGNATURE_RAW_RSA:
                    {
                        signedDataB = PoorSignature.signRaw(md.digest(), new String(privateKeyAliasBytes), SIGNATURE_OID, hStore);
                    }
                    break;
                case PoorSettings.SIGNATURE_PKCS_7:
                    {
                        signedDataB = PoorSignature.signPkcs7(baos.toByteArray(), new String(privateKeyAliasBytes), SIGNATURE_OID, hStore, includeSigner, detachedSignature);
                    }
            }
            return signedDataB;
        }
        mode = MODE_UNINITIALIZED;
        return null;
    }
