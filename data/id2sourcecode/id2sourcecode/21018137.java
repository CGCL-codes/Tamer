    public boolean login(String username, String timestamp, String secret) throws IOException, NoSuchAlgorithmException {
        int i;
        byte[] digest;
        StringBuffer buffer, digestBuffer;
        MessageDigest md5;
        if (getState() != AUTHORIZATION_STATE) return false;
        md5 = MessageDigest.getInstance("MD5");
        timestamp += secret;
        digest = md5.digest(timestamp.getBytes());
        digestBuffer = new StringBuffer(128);
        for (i = 0; i < digest.length; i++) digestBuffer.append(Integer.toHexString(digest[i] & 0xff));
        buffer = new StringBuffer(256);
        buffer.append(username);
        buffer.append(' ');
        buffer.append(digestBuffer.toString());
        if (sendCommand(POP3Command.APOP, buffer.toString()) != POP3Reply.OK) return false;
        setState(TRANSACTION_STATE);
        return true;
    }
