    public static byte[] getHash(byte[] inBuf, int inOff, int inLen) {
        synchronized (shaSync) {
            try {
                if (sha == null) {
                    sha = MessageDigest.getInstance("SHA-1");
                }
                sha.reset();
                byte[] hash = new byte[20];
                sha.update(inBuf, inOff, inLen);
                sha.digest(hash, 0, hash.length);
                return hash;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
