    public static String get(String path, String algo) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        MessageDigest md = MessageDigest.getInstance(algo);
        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = fileInputStream.read(buffer)) > 0) {
            md.update(buffer, 0, read);
        }
        return getHexString(md.digest());
    }
