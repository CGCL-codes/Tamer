    public void startCrypt(byte[] key) {
        System.err.println("WoWconn.startCrypt() " + key.length);
        byte skip1k[] = new byte[1024];
        HMac hash = new HMac(new Sha160());
        hash.init(decSeed);
        hash.update(key, 0, key.length);
        dCipher = new ARCFour();
        dCipher.init(hash.digest());
        dCipher.nextBytes(skip1k);
        hash = new HMac(new Sha160());
        hash.init(encSeed);
        hash.update(key, 0, key.length);
        eCipher = new ARCFour();
        eCipher.init(hash.digest());
        eCipher.nextBytes(skip1k);
    }
