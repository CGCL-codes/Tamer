    protected byte[] engineSign() throws SignatureException {
        byte[] hashToBeSigned = digestEngine_.digest();
        DigestInfo digestInfoEngine = new DigestInfo(AlgorithmID.sha1, hashToBeSigned);
        byte[] toBeEncrypted = digestInfoEngine.toByteArray();
        byte[] signatureValue = null;
        try {
            session_.signInit(signatureMechanism_, signatureKey_);
            signatureValue = session_.sign(toBeEncrypted);
        } catch (TokenException ex) {
            throw new SignatureException(ex.toString());
        }
        return signatureValue;
    }
