    public String sha1() {
        return Base64.encodeBytes(hash.digest(getSha1Data()));
    }
