    private byte[] getDigest(File file, Configuration conf) throws IOException {
        byte[] def = FileUtil.getFileContents(file);
        MessageDigest alg;
        try {
            alg = MessageDigest.getInstance(conf.digestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException(e);
        }
        return alg.digest(def);
    }
