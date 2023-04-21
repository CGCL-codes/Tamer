    private static byte[] createChecksum(String filename) {
        try {
            final BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filename));
            byte[] buffer = new byte[1024];
            final MessageDigest complete = MessageDigest.getInstance("SHA1");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            fis.close();
            return complete.digest();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
