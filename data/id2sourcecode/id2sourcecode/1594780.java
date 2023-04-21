    private void uncompressData(File tempDir, ZipInputStream in, String fullPrefix) throws IOException {
        String prefix = fullPrefix;
        String remainingPrefix = null;
        if (fullPrefix.indexOf(SUBZIP_SEPARATOR) != -1) {
            int pos = fullPrefix.indexOf(SUBZIP_SEPARATOR);
            prefix = fullPrefix.substring(0, pos);
            remainingPrefix = fullPrefix.substring(pos + SUBZIP_SEPARATOR.length());
        }
        ZipEntry e;
        while ((e = in.getNextEntry()) != null) {
            String filename = e.getName().replace('\\', '/');
            if (remainingPrefix != null) {
                if (filename.equals(prefix)) {
                    ZipInputStream subZip = openZipStream(in, filename);
                    uncompressData(tempDir, subZip, remainingPrefix);
                }
            } else if (filename.startsWith(prefix) && !e.isDirectory()) {
                filename = filename.substring(prefix.length());
                File destFile = new File(tempDir, filename);
                if (filename.indexOf('/') != -1) destFile.getParentFile().mkdirs();
                FileUtils.copyFile(in, destFile);
                if (e.getTime() != -1) {
                    destFile.setLastModified(e.getTime());
                    dataTimeStamp = Math.max(dataTimeStamp, e.getTime());
                }
            }
        }
    }
