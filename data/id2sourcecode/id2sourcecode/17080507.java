    public String computeHash(String filename) throws NoSuchAlgorithmException, IOException {
        FileInputStream fis = new FileInputStream(new File(filename));
        MessageDigest md = MessageDigest.getInstance("SHA1");
        DigestInputStream dis = new DigestInputStream(fis, md);
        while (dis.read(buf) != -1) ;
        String hash = DocumentStore.toHexString(md.digest());
        log.info("SHA1 read digest:" + hash);
        return hash;
    }
