    public static String getUniqueID() {
        String randomNum = new Integer(prng.nextInt()).toString();
        byte[] result = sha.digest(randomNum.getBytes());
        return new String(result);
    }
