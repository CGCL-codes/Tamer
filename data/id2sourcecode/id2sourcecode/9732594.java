    private void performUpload(File targetDir, FileItem fileItem) throws Exception, IOException {
        String fileName = fileItem.getName();
        File saveLocation = new File(targetDir, fileName);
        long sizeInBytes = fileItem.getSize();
        log.debug("Uploading file, fileName: '" + fileName + "', size: '" + sizeInBytes + "'");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = fileItem.getInputStream();
            outputStream = new FileOutputStream(saveLocation);
            final int bytesPerSecond = 100 * 1024;
            byte[] buffer = new byte[bytesPerSecond];
            for (int read = -1, written = 0; (read = inputStream.read(buffer)) > 0; ) {
                outputStream.write(buffer, 0, read);
                written += read;
                Thread.sleep(1000);
            }
            log.info("Uploaded file '" + saveLocation + "'.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }
