    @Deprecated
    public void writeFile(InputStream in, String fileName, String fileKey) throws IOException {
        int maxlength = 1000000;
        File uploadedFile = new File(fileKey);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uploadedFile, uploadedFile.exists());
            byte[] buffer = new byte[maxlength];
            int readBytes = in.read(buffer);
            if (readBytes < 0) {
                fos.write(new byte[0]);
            } else {
                while (readBytes > -1) {
                    fos.write(buffer, 0, readBytes);
                    buffer = new byte[maxlength];
                    readBytes = in.read(buffer);
                }
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        logger.logIt("Wrote " + uploadedFile.getAbsolutePath() + " to server");
    }
