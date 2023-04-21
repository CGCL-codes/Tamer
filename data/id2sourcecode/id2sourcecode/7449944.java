    public static String hashString(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("The SHA Algorithm could not be found", e);
        }
        byte[] bytes = input.getBytes();
        md.update(bytes);
        String hashed = convertToHex(md.digest());
        hashed = reduceHash(hashed, 8);
        return hashed;
    }
