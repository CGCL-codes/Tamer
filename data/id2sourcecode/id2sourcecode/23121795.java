    private static String extractFromJar(String fileNameInArchive, String archivePath) throws SecurityException, FileNotFoundException, IOException {
        URL url = new URL(archivePath);
        InputStream inputStream = url.openStream();
        ZipInputStream zipStream = new ZipInputStream(inputStream);
        ZipEntry entry = null;
        do {
            if (entry != null) zipStream.closeEntry();
            entry = zipStream.getNextEntry();
        } while (!entry.getName().endsWith(fileNameInArchive) && entry != null);
        if (entry == null) {
            zipStream.closeEntry();
            zipStream.close();
            inputStream.close();
            throw new FileNotFoundException("Unable to locate " + fileNameInArchive + " in " + archivePath);
        }
        String directory = System.getProperty("user.home") + System.getProperty("file.separator") + ".facileTemp";
        new File(directory).mkdirs();
        archivePath = directory + System.getProperty("file.separator") + CLASS_NAME + ".dll";
        FileOutputStream outputStream = new FileOutputStream(archivePath);
        for (int c = zipStream.read(); c != -1; c = zipStream.read()) {
            outputStream.write(c);
        }
        zipStream.closeEntry();
        zipStream.close();
        inputStream.close();
        outputStream.close();
        return "file:/" + archivePath;
    }
