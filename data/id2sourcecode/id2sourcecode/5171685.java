    public void unzipTestCase(byte[] testCaseBytes, File testCaseDir) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(testCaseBytes);
        ZipInputStream zin = new ZipInputStream(bin);
        ZipEntry zEntry;
        while ((zEntry = zin.getNextEntry()) != null) {
            String zEntryName = zEntry.getName();
            if (zEntryName.startsWith("/")) zEntryName = zEntryName.substring(1); else if (zEntryName.startsWith("./")) zEntryName = zEntryName.substring(2);
            zEntryName = testCaseDir + File.separator + zEntryName;
            File zipFile = new File(zEntryName);
            if (zEntry.isDirectory()) {
                UDPLogger.trace("next entry: " + zEntryName + " (directory)");
                if (!zipFile.exists()) zipFile.mkdirs();
            } else {
                UDPLogger.trace("next zip entry: " + zEntry);
                UDPLogger.trace("storing entry to file " + zipFile);
                File zipFileParent = zipFile.getParentFile();
                if (zipFileParent != null) {
                    if (!zipFileParent.exists()) zipFileParent.mkdirs();
                }
                if (zipFile.exists()) zipFile.delete();
                zipFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(zipFile);
                while (true) {
                    byte buf[] = new byte[4096];
                    int read = zin.read(buf, 0, 4096);
                    if (read > -1) fout.write(buf, 0, read); else break;
                }
                fout.flush();
                fout.close();
            }
        }
    }
