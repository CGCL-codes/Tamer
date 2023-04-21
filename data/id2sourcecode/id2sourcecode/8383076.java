    public void close() throws IOException {
        if (myDigestResult == null) {
            myDigestResult = myDigest.digest();
        }
        if (myCloseTarget) {
            myTarget.close();
        }
    }
