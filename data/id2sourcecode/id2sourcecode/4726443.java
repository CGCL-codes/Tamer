    public String getDigestValue(String digestSpec) throws Exception {
        String docval = toString();
        byte[] digestBytes = MessageDigest.getInstance(digestSpec).digest(docval.getBytes());
        String digestStr = Utils.toHex(digestBytes);
        return digestStr;
    }
