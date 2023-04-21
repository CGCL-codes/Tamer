    public static byte[] digest(File f) throws IOException, NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA").digest(load(f));
    }
