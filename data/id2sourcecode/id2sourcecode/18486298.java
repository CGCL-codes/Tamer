    public boolean setKey(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            key = null;
            return false;
        }
        byte[] input = s.getBytes();
        md.update(input);
        key = md.digest();
        return true;
    }
