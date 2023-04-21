    public static String getDigest(String path, String fullPath) throws NoSuchAlgorithmException, FileNotFoundException, IOException, UnsupportedEncodingException {
        if (path.endsWith(".jar")) return getJarDigest(fullPath);
        MessageDigest digest = getDigest();
        digest.update(path.getBytes("ASCII"));
        if (fullPath != null) updateDigest(new FileInputStream(fullPath), digest);
        return toHex(digest.digest());
    }
