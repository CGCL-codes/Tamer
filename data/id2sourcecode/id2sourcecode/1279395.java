    private final boolean authenticateConnection() {
        byte[] authChallengePhrase = generateChallengePhrase(this.toString());
        sendMessage(Protocol.MSG_AUTHCHALLENGE, authChallengePhrase);
        try {
            byte[] authSharedKey = new byte[0];
            try {
                authSharedKey = nodeConfig.getAuthSharedKeyClient().getBytes(remoteClient.getStringEncoding());
            } catch (UnsupportedEncodingException e) {
                logManager.writeToLog(1, "NET", "[" + threadID + "] Auth error: " + e.getMessage());
            }
            byte[] authCorrectPreDigest = new byte[authChallengePhrase.length + authSharedKey.length];
            System.arraycopy(authChallengePhrase, 0, authCorrectPreDigest, 0, authChallengePhrase.length);
            System.arraycopy(authSharedKey, 0, authCorrectPreDigest, authChallengePhrase.length, authSharedKey.length);
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                logManager.writeToLog(1, "NET", "[" + threadID + "] Auth error: " + e.getMessage());
            }
            byte[] authCorrectResponse = md.digest(authCorrectPreDigest);
            Message authResponse = readMessage();
            if (authResponse.getMessageFlag() != Protocol.MSG_AUTHCHALLENGE_RESP) {
                return false;
            }
            byte[] authResponseBytes = authResponse.getMessageBytes();
            if (authCorrectResponse.length != authResponseBytes.length) {
                return false;
            }
            for (int c = 0; c < authCorrectResponse.length; c++) {
                if (authCorrectResponse[c] != authResponseBytes[c]) {
                    return false;
                }
            }
            sendMessage(Protocol.MSG_OK, "Authentication accepted.");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
