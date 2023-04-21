    @Test
    public void testWriteContent() throws Exception {
        Flosser flosser = new Flosser(testprefix);
        CCNHandle thandle = CCNHandle.open();
        CCNFlowControl fc = new CCNFlowControl(testprefix, thandle);
        KeyManager km = KeyManager.getDefaultKeyManager();
        CCNHandle handle = CCNHandle.open(km);
        PublicKeyCache kr = new PublicKeyCache();
        KeyServer ks = new KeyServer(handle);
        for (int i = 0; i < KEY_COUNT; ++i) {
            ks.serveKey(keyLocs[i].name().name(), pairs[i].getPublic(), km.getDefaultKeyID(), null);
        }
        Random rand = new Random();
        for (int i = 0; i < DATA_COUNT_PER_KEY; ++i) {
            byte[] buf = new byte[1024];
            rand.nextBytes(buf);
            byte[] digest = CCNDigestHelper.digest(buf);
            ContentName dataName = new ContentName(dataprefix, digest);
            for (int j = 0; j < KEY_COUNT; ++j) {
                SignedInfo si = new SignedInfo(publishers[j], keyLocs[j]);
                ContentObject co = new ContentObject(dataName, si, buf, pairs[j].getPrivate());
                System.out.println("Key " + j + ": " + publishers[j] + " signed content " + i + ": " + dataName);
                fc.put(co);
            }
        }
        for (int i = 0; i < KEY_COUNT; ++i) {
            System.out.println("Attempting to retrieive key " + i + ":");
            PublicKey pk = kr.getPublicKey(publishers[i], keyLocs[i], SystemConfiguration.getDefaultTimeout(), handle);
            if (null == pk) {
                System.out.println("..... failed.");
            } else {
                System.out.println("..... got it! Correct key? " + (pk.equals(pairs[i].getPublic())));
            }
        }
        flosser.stop();
        thandle.close();
    }
