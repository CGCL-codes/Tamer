    private byte[] calculateSHA1(byte[] input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            lt.warning("method calculateKSeed(" + HexString.hexify(input) + ") throws SuchAlgorithmException", this);
        }
        md.update(input);
        return md.digest();
    }
