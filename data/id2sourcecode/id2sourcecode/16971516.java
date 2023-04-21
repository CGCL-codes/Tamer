    public static byte[] encrypt(String sz) {
        MessageDigest oMessageDigest;
        try {
            oMessageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Can't instantiate SHA message digest algorithm");
            e.printStackTrace();
            throw new RuntimeException("Can't instantiate SHA message digest algorithm");
        }
        oMessageDigest.reset();
        return oMessageDigest.digest(sz.getBytes());
    }
