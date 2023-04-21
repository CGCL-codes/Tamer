    public static File extractZipEntry(final String zipFilename, final String locationWithinZip, final File destinationDir) throws IOException {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilename);
            ZipEntry zipEntry = getZipEntry(zipFile, locationWithinZip);
            InputStream is = zipFile.getInputStream(zipEntry);
            File destinationFile = new File(destinationDir, zipEntry.getName());
            if (destinationFile.exists()) {
                destinationFile.delete();
            } else {
                File parentDir = destinationFile.getParentFile();
                if ((parentDir != null) && (!parentDir.exists())) {
                    destinationFile.mkdirs();
                }
            }
            FileUtils.copyFile(is, destinationFile);
            return destinationFile;
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
            }
        }
    }
