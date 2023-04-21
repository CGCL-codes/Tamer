    private static byte[] calculateSHA1(byte[] input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("method calculateKSeed(" + HexString.hexify(input) + ") throws SuchAlgorithmException");
        }
        md.update(input);
        return md.digest();
    }
