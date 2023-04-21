    private byte[] getDigest(byte[] magicKey, byte[] hash, boolean doModify) throws NoSuchAlgorithmException, NoSuchProviderException {
        int i;
        byte[] hashXor1 = new byte[64];
        for (i = 0; i < hash.length; i++) {
            hashXor1[i] = (byte) (hash[i] ^ 0x36);
        }
        Arrays.fill(hashXor1, i, 64, (byte) 0x36);
        byte[] hashXor2 = new byte[64];
        for (i = 0; i < hash.length; i++) {
            hashXor2[i] = (byte) (hash[i] ^ 0x5c);
        }
        Arrays.fill(hashXor2, i, 64, (byte) 0x5c);
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1", "YmsgMessageDigest");
        sha1.update(hashXor1, 0, 64);
        if (doModify) {
            ((vavi.net.im.protocol.ymsg.auth.YmsgSHA1) sha1).setBitCount(0x1ff);
        }
        sha1.update(magicKey, 0, 4);
        byte[] digest1 = sha1.digest();
        MessageDigest sha2 = MessageDigest.getInstance("SHA");
        sha2.update(hashXor2, 0, 64);
        sha2.update(digest1, 0, 20);
        byte[] digest2 = sha2.digest();
        return digest2;
    }
