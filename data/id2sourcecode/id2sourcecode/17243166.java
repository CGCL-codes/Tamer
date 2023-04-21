    public static byte[] getSHA1DigestFromFile(File inFile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        FileInputStream in = new FileInputStream(inFile);
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        while (bytesRead != -1) {
            digest.update(buffer, 0, bytesRead);
            bytesRead = in.read(buffer);
        }
        in.close();
        return digest.digest();
    }
