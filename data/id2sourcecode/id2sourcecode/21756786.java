    public static boolean extractZipFile(String zipFilePath, final byte[] readBuffer, String destinationPath) throws IOException {
        File file = new File(zipFilePath);
        if (file.exists() && !file.isFile()) {
            return false;
        }
        if (destinationPath.equalsIgnoreCase("")) {
            destinationPath = ".";
        }
        File directory = new File(destinationPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return false;
            }
        } else if (directory.exists()) {
            if (!directory.isDirectory()) {
                return false;
            }
        }
        InputStream fileStream = null;
        ZipInputStream zipReader = null;
        try {
            fileStream = Channels.newInputStream(new FileInputStream(file).getChannel());
            zipReader = new ZipInputStream(fileStream);
            ZipEntry zipEntry;
            while ((zipEntry = zipReader.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    if (!extractDirectoryFromZip(zipEntry, destinationPath)) {
                        return false;
                    }
                } else {
                    if (!extractFileFromZip(zipReader, zipEntry, readBuffer, destinationPath)) {
                        return false;
                    }
                }
                zipReader.closeEntry();
            }
        } finally {
            if (zipReader != null) {
                zipReader.close();
            }
        }
        return true;
    }
