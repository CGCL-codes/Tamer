    public void setHashKey(int number, int generation) {
        md5.reset();
        extra[0] = (byte) number;
        extra[1] = (byte) (number >> 8);
        extra[2] = (byte) (number >> 16);
        extra[3] = (byte) generation;
        extra[4] = (byte) (generation >> 8);
        md5.update(mkey);
        key = md5.digest(extra);
        keySize = mkey.length + 5;
        if (keySize > 16) keySize = 16;
    }
