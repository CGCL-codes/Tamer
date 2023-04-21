    private static byte[] _part4Hash(String target, byte[] magicValue, boolean hackSha1) throws NoSuchAlgorithmException {
        byte[] xor1 = _part4Xor(target, 0x36);
        byte[] xor2 = _part4Xor(target, 0x5c);
        if (DB) {
            dump("P4.2", xor1);
            dump("P4.2", xor2);
            dump("P4.2", magicValue);
        }
        SHA1 sha1 = new SHA1();
        sha1.update(xor1);
        if (hackSha1) sha1.setBitCount(0x1ff);
        sha1.update(magicValue);
        byte[] digest1 = sha1.digest();
        sha1.reset();
        sha1.update(xor2);
        sha1.update(digest1);
        byte[] digest2 = sha1.digest();
        if (DB) {
            dump("P4.3", digest1);
            dump("P4.3", digest2);
        }
        return digest2;
    }
