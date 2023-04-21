    public static void zipFiles(String zipFileName, String fileList[], String fileNameInZip[]) throws IOException {
        File file;
        if ((file = new File(zipFileName)).exists()) {
            delete(file);
        }
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName));
        for (int i = 0; i < fileList.length; i++) {
            file = new File(fileList[i]);
            if (file.isDirectory()) {
                fileNameInZip[i] = fileNameInZip[i] + '/';
                ZipEntry zipEntry = new ZipEntry(fileNameInZip[i]);
                zipEntry.setSize(0);
                zipFile.putNextEntry(zipEntry);
            } else {
                ZipEntry zipEntry = new ZipEntry(fileNameInZip[i]);
                zipEntry.setSize(file.length());
                zipFile.putNextEntry(zipEntry);
                writeZipBytes(zipFile, file);
            }
        }
        zipFile.flush();
        zipFile.close();
    }
