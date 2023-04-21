    private byte[] applyDigest(String digestAlg, byte[] bytes) throws NoSuchAlgorithmException {
        System.out.println("Applying digest algorithm...");
        MessageDigest md = MessageDigest.getInstance(digestAlg);
        md.update(bytes);
        return md.digest();
    }
