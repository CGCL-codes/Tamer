    public static boolean check(byte[] location, byte[] publickey, byte[] puzzlekey, int m) {
        MessageDigest md1 = null, md2 = null, md3 = null;
        try {
            md1 = MessageDigest.getInstance("SHA");
            md2 = MessageDigest.getInstance("SHA");
            md3 = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA support!");
        }
        md1.update(publickey);
        md2.update(puzzlekey);
        if (!HerbivoreUtil.equalsBits(md1.digest(), md2.digest(), m)) return false;
        md3.update(publickey);
        md3.update(puzzlekey);
        byte[] digest = md3.digest();
        if (location.length < 128 / 8) return false;
        for (int i = 0; i < location.length; ++i) if (location[i] != digest[i]) return false;
        return true;
    }
