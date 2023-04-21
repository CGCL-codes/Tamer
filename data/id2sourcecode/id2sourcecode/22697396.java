    private static void resetLocalPRNG() {
        if (Configuration.DEBUG) log.entering(ICMRandomSpi.class.getName(), "resetLocalPRNG");
        HashMap attributes = new HashMap();
        attributes.put(ICMGenerator.CIPHER, Registry.AES_CIPHER);
        byte[] key = new byte[128 / 8];
        Random rand = new Random(System.currentTimeMillis());
        rand.nextBytes(key);
        attributes.put(IBlockCipher.KEY_MATERIAL, key);
        int aesBlockSize = 128 / 8;
        byte[] offset = new byte[aesBlockSize];
        rand.nextBytes(offset);
        attributes.put(ICMGenerator.OFFSET, offset);
        int ndxLen = 0;
        int limit = aesBlockSize / 2;
        while (ndxLen < 1 || ndxLen > limit) ndxLen = rand.nextInt(limit + 1);
        attributes.put(ICMGenerator.SEGMENT_INDEX_LENGTH, Integer.valueOf(ndxLen));
        byte[] index = new byte[ndxLen];
        rand.nextBytes(index);
        attributes.put(ICMGenerator.SEGMENT_INDEX, new BigInteger(1, index));
        prng.setup(attributes);
        if (Configuration.DEBUG) log.exiting(ICMRandomSpi.class.getName(), "resetLocalPRNG");
    }
