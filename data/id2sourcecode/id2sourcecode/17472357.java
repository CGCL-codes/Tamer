    public static File readFileFromZipFile(ZipEntry entry, ZipFile zipFile) throws IOException {
        long size = entry.getSize();
        if (size > 0) {
            BufferedInputStream reader = new BufferedInputStream(zipFile.getInputStream(entry));
            String fileName = new File(entry.getName()).getName();
            File outputFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(fileName, "tmp");
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE);
            byte readBytes[] = new byte[BUFFER_SIZE];
            int readByteCount;
            while ((readByteCount = reader.read(readBytes, 0, BUFFER_SIZE)) != -1) {
                output.write(readBytes, 0, readByteCount);
            }
            output.close();
            return outputFile;
        } else {
            return null;
        }
    }
