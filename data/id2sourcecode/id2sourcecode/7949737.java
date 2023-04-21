    public static String createMD5Digest(File file) throws IOException {
        String result = "";
        InputStream inputStream = new FileInputStream(file);
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) >= 0) {
                messageDigest.update(buffer, 0, bytesRead);
            }
            inputStream.close();
            result = new String(Hex.encode(messageDigest.digest()));
            logger.debug("The MD5 Digest for: " + file + " is: " + result);
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }
