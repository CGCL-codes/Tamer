    public static byte[] chapMD5(byte id, byte[] Password, byte[] Challenge) {
        IMessageDigest md = HashFactory.getInstance("MD5");
        md.update(id);
        md.update(Password, 0, Password.length);
        md.update(Challenge, 0, Challenge.length);
        return md.digest();
    }
