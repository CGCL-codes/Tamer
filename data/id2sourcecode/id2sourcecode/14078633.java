    public boolean verifyTimestampImprint() throws NoSuchAlgorithmException {
        if (timeStampToken == null) return false;
        TimeStampTokenInfo info = timeStampToken.getTimeStampInfo();
        MessageImprint imprint = info.toASN1Structure().getMessageImprint();
        String algOID = info.getMessageImprintAlgOID().getId();
        byte[] md = MessageDigest.getInstance(algOID).digest(digest);
        byte[] imphashed = imprint.getHashedMessage();
        boolean res = Arrays.equals(md, imphashed);
        return res;
    }
