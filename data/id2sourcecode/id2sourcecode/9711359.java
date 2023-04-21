    public static byte[] getHash(File f) throws Exception {
        MessageDigest hashSum = MessageDigest.getInstance("SHA-256");
        final BufferedInputStream fb = new BufferedInputStream(new FileInputStream(f));
        byte[] buffer = new byte[blockSize];
        int readSize = fb.read(buffer);
        while (readSize > 0) {
            hashSum.update(buffer, 0, readSize);
            readSize = fb.read(buffer);
        }
        fb.close();
        byte[] fileHash = new byte[hashSum.getDigestLength()];
        fileHash = hashSum.digest();
        return fileHash;
    }
