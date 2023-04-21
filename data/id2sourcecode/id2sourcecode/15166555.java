    public static final byte[] generateHash(String clearText1, String clearText2) throws Exception {
        String id;
        if (clearText2 == null) {
            id = clearText1;
        } else {
            id = clearText1 + clearText2;
        }
        byte[] buffer = id.getBytes();
        MessageDigest algorithm = null;
        algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(buffer);
        byte[] digest1 = algorithm.digest();
        return digest1;
    }
