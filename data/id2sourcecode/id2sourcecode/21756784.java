    private static boolean addDirectoryToZip(ZipOutputStream zipWriter, File zipArchive, File directory, final byte[] readBuffer, String currentPath) throws IOException {
        if (!directory.canRead()) {
            return false;
        }
        ZipEntry zipDirectory = new ZipEntry(currentPath + '/');
        zipWriter.putNextEntry(zipDirectory);
        zipWriter.closeEntry();
        zipWriter.flush();
        for (File child : directory.listFiles()) {
            if (child.isFile()) {
                if (!addFileToZip(zipWriter, zipArchive, child, readBuffer, currentPath + '/')) {
                    return false;
                }
            } else if (child.isDirectory()) {
                if (!addDirectoryToZip(zipWriter, zipArchive, child, readBuffer, currentPath + '/' + child.getName())) {
                    return false;
                }
            }
        }
        return true;
    }
