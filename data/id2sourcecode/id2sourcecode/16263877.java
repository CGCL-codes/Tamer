    public static byte[] getHash(byte[] inBuf, int inOff, int inLen) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.reset();
            byte[] hash = new byte[20];
            hash = sha.digest(inBuf);
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
