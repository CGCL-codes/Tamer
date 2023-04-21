    private long bloomFilterMask(String word, int lineNumber) {
        long retval = 0;
        MessageDigest md5 = null;
        byte[] md5digest = null;
        MessageDigest sha1 = null;
        byte[] sha1digest = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
        }
        String firstHash = word + "|" + lineNumber;
        String secondHash = lineNumber + "|" + word;
        int bitOne = 0;
        md5digest = md5.digest(firstHash.getBytes());
        ;
        for (int i = 0; i < md5.getDigestLength(); i++) {
            bitOne ^= md5digest[i];
        }
        bitOne %= bitsPerBitmask;
        retval |= (long) 1 << bitOne;
        int bitTwo = 0;
        md5digest = md5.digest(secondHash.getBytes());
        ;
        for (int i = 0; i < md5.getDigestLength(); i++) {
            bitTwo ^= md5digest[i];
        }
        bitTwo %= bitsPerBitmask;
        retval |= (long) 1 << bitTwo;
        int bitThree = 0;
        sha1digest = sha1.digest(firstHash.getBytes());
        for (int i = 0; i < sha1.getDigestLength(); i++) {
            bitThree ^= sha1digest[i];
        }
        bitThree %= bitsPerBitmask;
        retval |= (long) 1 << bitThree;
        int bitFour = 0;
        sha1digest = sha1.digest(secondHash.getBytes());
        for (int i = 0; i < sha1.getDigestLength(); i++) {
            bitFour ^= sha1digest[i];
        }
        bitFour %= bitsPerBitmask;
        retval |= (long) 1 << bitFour;
        return retval;
    }
