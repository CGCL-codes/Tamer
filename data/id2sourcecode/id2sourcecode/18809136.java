    protected byte[] makeResponseToken(int key1, int key2, byte[] token) throws NoSuchAlgorithmException {
        MessageDigest md5digest = MessageDigest.getInstance("MD5");
        for (Integer i = 0; i < 2; ++i) {
            byte[] asByte = new byte[4];
            int key = (i == 0) ? key1 : key2;
            asByte[0] = (byte) (key >> 24);
            asByte[1] = (byte) ((key << 8) >> 24);
            asByte[2] = (byte) ((key << 16) >> 24);
            asByte[3] = (byte) ((key << 24) >> 24);
            md5digest.update(asByte);
        }
        md5digest.update(token);
        return md5digest.digest();
    }
