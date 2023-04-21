    public static final byte[] sha1Hash(String text) {
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("SHA1");
            byte[] input = text.getBytes();
            byte[] hashValue = hash.digest(input);
            return hashValue;
        } catch (Exception ex) {
            Log.e("EE", "Exception in sha1hash: " + ex.toString());
        }
        return null;
    }
