    public static byte[] scramble411(String password, String seed) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] passwordHashStage1 = md.digest(password.getBytes());
        md.reset();
        byte[] passwordHashStage2 = md.digest(passwordHashStage1);
        md.reset();
        byte[] seedAsBytes = seed.getBytes();
        md.update(seedAsBytes);
        md.update(passwordHashStage2);
        byte[] toBeXord = md.digest();
        int numToXor = toBeXord.length;
        for (int i = 0; i < numToXor; i++) {
            toBeXord[i] = (byte) (toBeXord[i] ^ passwordHashStage1[i]);
        }
        return toBeXord;
    }
