    private static void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException {
        final File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                addFolderToZip(file, zip, baseName);
            } else {
                final String name = file.getAbsolutePath().substring(baseName.length());
                final ZipEntry zipEntry = new ZipEntry(name);
                zip.putNextEntry(zipEntry);
                final FileInputStream fileIn = new FileInputStream(file);
                StreamUtils.copy(fileIn, zip);
                StreamUtils.closeStream(fileIn);
                zip.closeEntry();
            }
        }
    }
