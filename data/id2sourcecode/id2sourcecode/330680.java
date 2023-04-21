    public byte[] createChecksum(String filename) {
        try {
            InputStream fis = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("SHA1");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            fis.close();
            return complete.digest();
        } catch (Exception e) {
            checksum.setText("Error:  " + e.getMessage());
        }
        return null;
    }
