    public static boolean createZipFile(String zipFilePath, int level, final byte[] readBuffer, String... sourcePaths) throws IOException {
        if (sourcePaths == null || sourcePaths.length == 0) {
            return false;
        }
        File zipArchive = new File(zipFilePath + ".tmp");
        if (zipArchive.exists()) {
            if (!zipArchive.isFile()) {
                return false;
            }
        } else if (!zipArchive.createNewFile()) {
            if (!zipArchive.delete() || !zipArchive.createNewFile()) {
                return false;
            }
        }
        OutputStream fileStream = null;
        ZipOutputStream zipWriter = null;
        try {
            fileStream = Channels.newOutputStream(new FileOutputStream(zipArchive).getChannel());
            zipWriter = new ZipOutputStream(fileStream);
            zipWriter.setLevel(level);
            for (String contentPath : sourcePaths) {
                File nextFile = new File(contentPath).getAbsoluteFile();
                if (nextFile.exists()) {
                    if (nextFile.isFile()) {
                        if (!addFileToZip(zipWriter, zipArchive, nextFile, readBuffer, "")) {
                            return false;
                        }
                    } else if (nextFile.isDirectory()) {
                        if (!addDirectoryToZip(zipWriter, zipArchive, nextFile, readBuffer, nextFile.getName())) {
                            return false;
                        }
                    }
                }
            }
        } finally {
            boolean error = false;
            try {
                if (zipWriter != null) {
                    zipWriter.flush();
                    zipWriter.finish();
                    zipWriter.close();
                } else {
                    error = true;
                }
            } catch (IOException e) {
                error = true;
            }
            if (fileStream != null) {
                fileStream.close();
            }
            if (error) {
                return false;
            }
        }
        File trueZipArchive = new File(zipFilePath);
        if (!zipArchive.renameTo(trueZipArchive)) {
            if (trueZipArchive.delete()) {
                if (!zipArchive.renameTo(trueZipArchive)) {
                    zipArchive.delete();
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
