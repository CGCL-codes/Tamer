    private final byte[] generateChallengePhrase(String sharedKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(sharedKey.getBytes(remoteClient.getStringEncoding()));
        } catch (Exception e) {
            logManager.writeToLog(1, "NET", "[" + threadID + "] Authentication error: " + e.getMessage());
        }
        return null;
    }
