    public static String computeDigest(URL url, String algorithm) throws IOException, NoSuchAlgorithmException {
        return computeDigest(url.openStream(), algorithm);
    }
