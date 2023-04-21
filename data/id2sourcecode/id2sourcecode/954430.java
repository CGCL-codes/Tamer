    byte[] checksumFile(File f) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(f);
        byte[] buf = new byte[8192];
        int howMany = 0;
        while (howMany >= 0) {
            howMany = fis.read(buf);
            if (howMany >= 0) {
                md.update(buf, 0, howMany);
            }
        }
        return md.digest();
    }
