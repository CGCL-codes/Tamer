    public String getDigestValue() throws Exception {
        String docval = toString();
        byte[] digestBytes = MessageDigest.getInstance("SHA-1").digest(docval.getBytes());
        String digestStr = Utils.toHex(digestBytes);
        return digestStr;
    }
