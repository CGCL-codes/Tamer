    public static String generateDigest(String idPassword) throws NoSuchAlgorithmException {
        String parts[] = idPassword.split(":", 2);
        byte digest[] = MessageDigest.getInstance("SHA1").digest(idPassword.getBytes());
        return parts[0] + ":" + base64Encode(digest);
    }
