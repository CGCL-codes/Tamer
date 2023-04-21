    static byte[] fileChecksum(File file) throws IOException {
        MessageDigest mdfour = null;
        try {
            mdfour = MessageDigest.getInstance("BrokenMD4");
        } catch (java.security.NoSuchAlgorithmException nse) {
            throw new Error(nse);
        }
        FileInputStream fin = new FileInputStream(file);
        byte[] buf = new byte[4096];
        int len;
        while ((len = fin.read(buf)) != -1) {
            mdfour.update(buf, 0, len);
        }
        return mdfour.digest();
    }
