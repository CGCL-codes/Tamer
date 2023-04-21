    public String getUniqueSha1() throws NoSuchAlgorithmException {
        if (uniqueSha1 == null) {
            if (md == null) md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] uniqueKey;
            uniqueKey = msg.getBytes();
            uniqueSha1 = StringUtils.hexEncode(md.digest(uniqueKey));
        }
        return uniqueSha1;
    }
