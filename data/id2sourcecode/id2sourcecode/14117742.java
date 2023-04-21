    public static Result get(final String algorithm, final InputStream input) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        DigestInputStream dis = new DigestInputStream(input, digest);
        FS.copyStream(dis, null);
        Result result = new Result();
        result.digest = digest.digest();
        return result;
    }
