    public static byte[] createChecksum(String fileName) {
        InputStream fis;
        MessageDigest complete = null;
        try {
            fis = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return complete.digest();
    }
