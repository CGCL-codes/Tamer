    public static Blowfish initBlowfish(PwsFileHeader aHeader, String aPassphrase) {
        SHA lDigester = new SHA();
        lDigester.update(aPassphrase.getBytes(), 0, aPassphrase.getBytes().length);
        lDigester.update(aHeader.getPwSalt(), 0, aHeader.getPwSalt().length);
        final byte[] lOperationalPwd = lDigester.digest();
        return new BlowfishCBC(new BlowfishLE(new BlowfishBasic(lOperationalPwd)), aHeader.getCbc());
    }
