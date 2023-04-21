    public byte[] getSha1() throws Exception {
        if (byteStream == null) throw new Exception("Sha1Bean:getSha1: byteStream is null");
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
        }
        byte[] result = md.digest(byteStream);
        return result;
    }
