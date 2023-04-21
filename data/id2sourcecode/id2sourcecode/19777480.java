    protected synchronized String createNewSessionId() {
        String sessionId = null;
        long sesIdSeed = ++sessionIdSeed;
        byte[] digestId = digest.digest(Long.toString(sesIdSeed).getBytes());
        sessionId = new String(Base64.encodeBase64(digestId));
        return sessionId;
    }
