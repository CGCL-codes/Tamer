    public void generateMD5ofFile(File file) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(file.toString().getBytes()));
            this.name = file.getName();
            this.location = file.getParent();
            this.size = file.getTotalSpace();
            this.md5 = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
