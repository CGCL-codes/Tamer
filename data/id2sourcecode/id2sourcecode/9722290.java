    public boolean dataChanged(byte[] ba) {
        boolean bRet = true;
        byte[] baHashNew = md.digest(ba);
        md.reset();
        if (null != baHashPrevious) {
            if (MessageDigest.isEqual(baHashNew, baHashPrevious)) {
                bRet = false;
            }
        }
        baHashPrevious = baHashNew;
        return bRet;
    }
