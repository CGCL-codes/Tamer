    private static byte[] getMd5Digest(File localFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("md5");
        InputStream fileStream = new FileInputStream(localFile);
        int count = 0;
        byte[] buffer = new byte[2048];
        while (count >= 0) {
            md.update(buffer, 0, count);
            count = fileStream.read(buffer);
        }
        fileStream.close();
        return md.digest();
    }
