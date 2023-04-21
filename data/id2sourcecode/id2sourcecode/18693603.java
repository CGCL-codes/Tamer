    public String calcSHA1(File file, MessageDigest messageDigest) throws Exception {
        FileInputStream inStream = new FileInputStream(file);
        byte[] buffer = new byte[64 * 1024];
        int length;
        long start = System.currentTimeMillis();
        while ((length = inStream.read(buffer)) != -1) {
            long start2 = System.currentTimeMillis();
            messageDigest.update(buffer, 0, length);
            long end2 = System.currentTimeMillis();
            Thread.sleep((end2 - start2) * 2);
        }
        inStream.close();
        byte[] shaDigest = messageDigest.digest();
        long end = System.currentTimeMillis();
        System.out.println("Digest: " + messageDigest.getClass() + " SHA1 time: " + (end - start) + " size: " + file.length());
        return Base32.encode(shaDigest);
    }
