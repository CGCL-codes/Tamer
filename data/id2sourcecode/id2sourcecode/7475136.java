    private boolean checkIntegrity(File src, File dest) throws IOException, NoSuchAlgorithmException {
        BufferedInputStream ins = new BufferedInputStream(new FileInputStream(src));
        BufferedInputStream ind = new BufferedInputStream(new FileInputStream(dest));
        MessageDigest md1 = MessageDigest.getInstance("MD5");
        MessageDigest md2 = MessageDigest.getInstance("MD5");
        int r = 0;
        byte[] buf1 = new byte[BUFFER_SIZE];
        byte[] buf2 = new byte[BUFFER_SIZE];
        while (r != -1) {
            r = ins.read(buf1);
            ind.read(buf2);
            md1.update(buf1);
            md2.update(buf2);
        }
        ins.close();
        ind.close();
        return MessageDigest.isEqual(md1.digest(), md2.digest());
    }
