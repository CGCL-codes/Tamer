    public String SHA1(File file) throws NoSuchAlgorithmException, IOException {
        if (!file.exists()) throw new FileNotFoundException(file.toString());
        MessageDigest md = MessageDigest.getInstance(SHA1);
        FileInputStream fis = new FileInputStream(file);
        byte[] dataBytes = new byte[1024];
        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        byte[] mdbytes = md.digest();
        return byteToHex(mdbytes);
    }
