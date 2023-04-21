    public boolean isHashAuthenticated(String userHash, String testHash) {
        testHash = HexString.toString(sha.digest(testHash.getBytes()));
        return testHash.equals(userHash);
    }
