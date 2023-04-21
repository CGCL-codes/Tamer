    public MessageDigest generateChecksum(File filename) throws Exception {
        MessageDigest complete = MessageDigest.getInstance("MD5");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int numRead = -1;
            while ((numRead = inputStream.read(buffer)) > 0) {
                complete.digest(buffer, 0, numRead);
            }
        } finally {
            try {
                inputStream.close();
            } catch (Exception ex) {
            }
        }
        return complete;
    }
