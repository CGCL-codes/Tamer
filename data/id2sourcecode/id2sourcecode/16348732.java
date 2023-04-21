    private static void extractZipFiles(String destinationDir, InputStream xeoAppZip) throws IOException {
        byte[] buf = new byte[8192];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(xeoAppZip);
        zipentry = zipinputstream.getNextEntry();
        while (zipentry != null) {
            String entryName = zipentry.getName();
            int n;
            File outFile = new File(destinationDir + entryName);
            if (zipentry.isDirectory()) {
                outFile.mkdirs();
            } else {
                FileOutputStream fileoutputstream;
                fileoutputstream = new FileOutputStream(outFile);
                while ((n = zipinputstream.read(buf)) > -1) fileoutputstream.write(buf, 0, n);
                fileoutputstream.close();
            }
            zipinputstream.closeEntry();
            zipentry = zipinputstream.getNextEntry();
        }
        zipinputstream.close();
    }
