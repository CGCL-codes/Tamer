    private static byte[] createChecksum(String filename) throws Exception {
        FileInputStream in = new FileInputStream(filename);
        byte[] buffer = new byte[BUFFER];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = in.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        in.close();
        return complete.digest();
    }
