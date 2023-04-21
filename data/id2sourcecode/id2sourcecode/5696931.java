    public static byte[] computeDigest(String hashAlgorithm, File pathname, final int chunkLength) throws NoSuchAlgorithmException, IOException {
        byte[] data = new byte[chunkLength];
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        FileInputStream in = new FileInputStream(pathname);
        int chunkBytesRead;
        while ((chunkBytesRead = in.read(data, 0, chunkLength)) != -1) {
            md.update(data, 0, chunkBytesRead);
        }
        in.close();
        return md.digest();
    }
