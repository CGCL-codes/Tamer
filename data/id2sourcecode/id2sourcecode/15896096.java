    private byte[] createChecksum(File file) throws Exception {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[(int) (file.length())];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }
