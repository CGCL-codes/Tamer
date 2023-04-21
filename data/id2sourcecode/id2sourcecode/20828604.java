    private void generateKeysBlock(byte[] clientRand, byte[] serverRand, byte[] masterSecret) throws GeneralSecurityException {
        byte[] expansion[] = { { 0x41 }, { 0x42, 0x42 }, { 0x43, 0x43, 0x43 }, { 0x44, 0x44, 0x44, 0x44 }, { 0x45, 0x45, 0x45, 0x45, 0x45 }, { 0x46, 0x46, 0x46, 0x46, 0x46, 0x46 }, { 0x47, 0x47, 0x47, 0x47, 0x47, 0x47, 0x47 } };
        byte[] blockSubExp = new byte[masterSecret.length + serverRand.length + clientRand.length];
        int offset = 0;
        System.arraycopy(masterSecret, 0, blockSubExp, offset, masterSecret.length);
        offset += masterSecret.length;
        System.arraycopy(serverRand, 0, blockSubExp, offset, serverRand.length);
        offset += serverRand.length;
        System.arraycopy(clientRand, 0, blockSubExp, offset, clientRand.length);
        for (int i = 0; i < (keyBlock.length >>> 4); i++) {
            md.update(masterSecret, 0, masterSecret.length);
            sd.update(expansion[i], 0, expansion[i].length);
            byte[] res = new byte[20];
            sd.update(blockSubExp, 0, blockSubExp.length);
            sd.digest(res, 0, res.length);
            md.update(res, 0, 20);
            md.digest(keyBlock, i << 4, md.getDigestLength());
        }
    }
