    public boolean verifyTimestampImprint() throws NoSuchAlgorithmException {
        if (timeStampToken == null) return false;
        MessageImprint imprint = timeStampToken.getTimeStampInfo().toTSTInfo().getMessageImprint();
        byte[] md = MessageDigest.getInstance("SHA-1").digest(digest);
        byte[] imphashed = imprint.getHashedMessage();
        boolean res = Arrays.equals(md, imphashed);
        return res;
    }
