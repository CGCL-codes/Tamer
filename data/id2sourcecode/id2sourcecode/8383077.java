    public String getDigest() {
        if (myDigestResult == null) {
            myDigestResult = myDigest.digest();
        }
        return SVNFileUtil.toHexDigest(myDigestResult);
    }
