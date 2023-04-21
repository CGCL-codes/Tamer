    public static void addFile(File file, ZipOutputStream outputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(file.getName());
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream.putNextEntry(zipEntry);
            StreamUtils.transfer(inputStream, outputStream);
            outputStream.closeEntry();
        } finally {
            StreamUtils.closeStream(inputStream);
        }
    }
