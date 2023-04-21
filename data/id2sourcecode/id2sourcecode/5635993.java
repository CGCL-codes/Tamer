    public static String md5(String s) {
        md.reset();
        md.update(s.getBytes());
        return HexString.bufferToHex(md.digest());
    }
