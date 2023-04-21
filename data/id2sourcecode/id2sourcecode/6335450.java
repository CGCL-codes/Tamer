    public String getZeroKHash(int sequence, byte[] token, byte[] password) {
        byte[] runningHash = sha.digest(password);
        sha.update(HexString.toString(runningHash).getBytes());
        runningHash = sha.digest(token);
        for (int i = 0; i < sequence; i++) {
            runningHash = sha.digest(HexString.toString(runningHash).getBytes());
        }
        return HexString.toString(runningHash);
    }
