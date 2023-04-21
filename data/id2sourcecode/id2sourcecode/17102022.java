    private static byte[] NtPasswordHash(byte[] Password) {
        byte PasswordHash[] = new byte[16];
        byte uniPassword[] = unicode(Password);
        IMessageDigest md = HashFactory.getInstance("MD4");
        md.update(uniPassword, 0, uniPassword.length);
        System.arraycopy(md.digest(), 0, PasswordHash, 0, 16);
        return PasswordHash;
    }
