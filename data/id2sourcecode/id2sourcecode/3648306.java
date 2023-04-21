    private String getMD5Sum(File sourceFile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(sourceFile);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) complete.update(buffer, 0, numRead);
        } while (numRead != -1);
        fis.close();
        byte[] b = complete.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        while (sb.length() < 32) sb.insert(0, '0');
        return sb.toString();
    }
