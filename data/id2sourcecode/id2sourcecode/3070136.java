    public static byte[] computeLocation(byte[] pubkey, byte[] puzzlekey) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA support!");
            System.exit(1);
        }
        md.update(pubkey);
        md.update(puzzlekey);
        byte[] digest = md.digest();
        byte[] subset = new byte[128 / 8];
        for (int i = 0; i < 128 / 8; ++i) subset[i] = digest[i];
        return subset;
    }
