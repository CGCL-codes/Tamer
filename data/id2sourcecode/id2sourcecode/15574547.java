    public static String digest(File f) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] data = fileBytes(f);
        byte[] digest = md.digest(data);
        String b64 = Base64.encodeBytes(digest);
        return b64;
    }
