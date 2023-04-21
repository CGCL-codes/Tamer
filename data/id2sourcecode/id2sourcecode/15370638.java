    public String generateNonce(String algorithm) {
        MessageDigest messageDigest = algorithms.get(algorithm);
        if (messageDigest == null) return "";
        long time = System.currentTimeMillis();
        long pad = random.nextLong();
        String nonceString = (new Long(time)).toString() + (new Long(pad)).toString();
        byte mdbytes[] = messageDigest.digest(nonceString.getBytes());
        return SipUtils.toHexString(mdbytes);
    }
