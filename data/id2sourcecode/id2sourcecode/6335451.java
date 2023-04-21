    public String getDigest(String streamID, String password) {
        sha.update(streamID.getBytes());
        return HexString.toString(sha.digest(password.getBytes()));
    }
