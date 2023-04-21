    private static void createZip(ZipOutputStream zos, File directory, int pathLength) throws IOException {
        File[] fileArray = directory.listFiles();
        for (int i = 0; i < fileArray.length; i++) {
            File currFile = fileArray[i];
            if (currFile.isDirectory()) {
                createZip(zos, currFile, pathLength);
            } else {
                String pathInZip = currFile.getAbsolutePath().substring(pathLength + 1);
                ZipEntry entry = new ZipEntry(pathInZip);
                zos.putNextEntry(entry);
                entry.setMethod(ZipEntry.DEFLATED);
                FileInputStream fis = new FileInputStream(currFile);
                copy(fis, zos);
                fis.close();
                zos.flush();
                zos.closeEntry();
            }
        }
    }
