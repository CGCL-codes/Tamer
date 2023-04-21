    private String hash(String algorithmName, File path) throws NoSuchAlgorithmException, IOException {
        MessageDigest algorithm = MessageDigest.getInstance(algorithmName);
        FileInputStream fis = new FileInputStream(path);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);
        while (dis.read() != -1) ;
        byte[] hash = algorithm.digest();
        return CommonUtils.hex(hash);
    }
