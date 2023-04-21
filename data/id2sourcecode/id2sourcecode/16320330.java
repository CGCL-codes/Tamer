    public static void verifyTimeStampTokenDigest(TimeStampToken timeStampToken, TimeStampDigestInput digestInput) throws XAdESValidationException {
        LOG.debug("digest verification: algo=" + timeStampToken.getTimeStampInfo().getMessageImprintAlgOID());
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(timeStampToken.getTimeStampInfo().getMessageImprintAlgOID());
        } catch (NoSuchAlgorithmException e) {
            throw new XAdESValidationException(e);
        }
        if (!Arrays.equals(md.digest(digestInput.getBytes()), timeStampToken.getTimeStampInfo().getMessageImprintDigest())) {
            throw new XAdESValidationException("Digest verification failure for " + "timestamp token");
        }
    }
