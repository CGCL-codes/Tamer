    byte[] encodePassword(String password) throws Exception {
        MessageDigest md;
        byte[] seed = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 };
        md = MessageDigest.getInstance("SHA-1");
        byte[] passwordHashStage1 = md.digest(password.getBytes("ASCII"));
        md.reset();
        byte[] passwordHashStage2 = md.digest(passwordHashStage1);
        md.reset();
        md.update(seed);
        md.update(passwordHashStage2);
        byte[] toBeXord = md.digest();
        int numToXor = toBeXord.length;
        for (int i = 0; i < numToXor; i++) {
            toBeXord[i] = (byte) (toBeXord[i] ^ passwordHashStage1[i]);
        }
        return toBeXord;
    }
