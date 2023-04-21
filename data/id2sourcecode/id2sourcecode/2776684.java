    public String checksum(File file, String algorithm) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        InputStream filein = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        int i;
        do {
            i = filein.read(buffer);
            if (i > 0) {
                digest.update(buffer, 0, i);
            }
        } while (i != -1);
        filein.close();
        return toHexString(digest.digest());
    }
