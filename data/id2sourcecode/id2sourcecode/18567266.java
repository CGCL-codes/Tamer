    protected byte[] createChecksum(File file) {
        int bytesRead = 1;
        byte[] readBytes = new byte[128];
        MessageDigest messageDigest;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            messageDigest = MessageDigest.getInstance("MD5");
            while (bytesRead > 0) {
                bytesRead = fileInputStream.read(readBytes);
                if (bytesRead > 0) {
                    messageDigest.update(readBytes, 0, bytesRead);
                }
            }
            fileInputStream.close();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return messageDigest.digest();
    }
