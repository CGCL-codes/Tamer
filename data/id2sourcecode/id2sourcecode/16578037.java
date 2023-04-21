    private void insertFileToZip(File childFile, ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(childFile.getName());
        FileInputStream fileInputStream = new FileInputStream(childFile);
        zipOutputStream.putNextEntry(zipEntry);
        StreamUtils.inputToOutput(fileInputStream, zipOutputStream);
        zipOutputStream.closeEntry();
        childFile.delete();
    }
