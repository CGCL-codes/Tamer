    public static byte[] calculatePassphraseHash(long aData, String aPassphrase) {
        final byte[] lPassphrasebytes = aPassphrase.getBytes();
        final SHA lDigester = new SHA();
        final byte[] lRndDataBytes = new byte[8];
        BlowfishUtil.unpackLE(aData, lRndDataBytes, 0);
        lDigester.update(lRndDataBytes, 0, 8);
        lDigester.update((byte) 0);
        lDigester.update((byte) 0);
        lDigester.update(lPassphrasebytes, 0, lPassphrasebytes.length);
        final byte[] lCalcKey = lDigester.digest();
        final Blowfish lFish = new BlowfishLE(new BlowfishBasic(lCalcKey));
        long lBlock = aData;
        for (int i = 0; i < 1000; i++) lBlock = lFish.encipher(lBlock);
        final byte[] lBlockBytes = new byte[8];
        BlowfishUtil.unpackLE(lBlock, lBlockBytes, 0);
        final SHAModified lSpecialDigester = new SHAModified();
        lSpecialDigester.update(lBlockBytes, 0, 8);
        lSpecialDigester.update((byte) 0);
        lSpecialDigester.update((byte) 0);
        return lSpecialDigester.digest();
    }
