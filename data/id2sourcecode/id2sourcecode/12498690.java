    public static OutputStream createOutputStream(File tempFile, String downloadFileNamePrefix, String downloadId, String fileExtension, boolean zipped) throws IOException {
        OutputStream outputStream = null;
        if (zipped) {
            outputStream = new ZipOutputStream(new FileOutputStream(tempFile));
            ((ZipOutputStream) outputStream).putNextEntry(new ZipEntry(downloadFileNamePrefix + downloadId + fileExtension));
        } else {
            outputStream = new FileOutputStream(tempFile);
        }
        return outputStream;
    }
