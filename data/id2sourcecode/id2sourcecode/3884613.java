    private void addFolderToZip(File folder, ZipFileOutputStream zip, String baseName) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                log.info("Adding [" + file.getName() + "]");
                addFolderToZip(file, zip, baseName);
            } else {
                String name = file.getAbsolutePath().substring(baseName.length());
                ZipFileEntry zipEntry = new ZipFileEntry(name);
                log.info("Adding [" + zipEntry.getFileName() + "]");
                zip.putNextEntry(zipEntry);
                writeDataBuffer(file);
                zip.closeEntry();
            }
        }
    }
