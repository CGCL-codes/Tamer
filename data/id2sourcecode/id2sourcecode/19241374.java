    public static byte[] MD5(String input) {
        try {
            return MessageDigest.getInstance("MD5").digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("Unable to create hash");
            return null;
        }
    }
