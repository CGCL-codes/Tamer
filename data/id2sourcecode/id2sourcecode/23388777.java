    public String generateNonce() {
        Date date = new Date();
        long time = date.getTime();
        Random rand = new Random();
        long pad = rand.nextLong();
        String nonceString = (Long.valueOf(time)).toString() + (Long.valueOf(pad)).toString();
        byte mdbytes[] = messageDigest.digest(nonceString.getBytes());
        return toHexString(mdbytes);
    }
