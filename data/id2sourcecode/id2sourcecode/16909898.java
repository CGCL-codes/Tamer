    public static String calculateNexthash(String string) {
        String s;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            BigInteger bi = new BigInteger(string, 16);
            md.update(bi.toByteArray());
            byte[] result = md.digest();
            BigInteger bir = new BigInteger(result);
            bir = bir.abs();
            s = bir.toString(16);
        } catch (Exception e) {
            s = "NULL";
        }
        return s;
    }
