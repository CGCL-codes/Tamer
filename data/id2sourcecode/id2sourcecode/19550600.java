    private String unPackZipIntoTmpPath(final String zipPath) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipPath);
        } catch (IOException e) {
        }
        ZipPathData zipPathData = zipPathEntryMap.get(zipPath);
        if (zipPathData == null) {
            String tmpPath = systemTmpPath + "aaspringTranslator/" + Math.abs(zipPath.hashCode()) + "_" + new Date().getTime();
            File tmpDir = new File(tmpPath);
            tmpDir.mkdirs();
            zipPathData = new ZipPathData();
            zipPathData.tmpPath = tmpPath;
            zipPathEntryMap.put(zipPath, zipPathData);
        }
        try {
            if (zipFile != null) {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    zipPathData.entriesByName.put(zipEntry.getName(), zipEntry);
                    File outputFile = new File(zipPathData.tmpPath + "/" + zipEntry.getName());
                    if (zipEntry.isDirectory()) outputFile.mkdirs(); else {
                        InputStream is = zipFile.getInputStream(zipEntry);
                        outputFile.getParentFile().mkdirs();
                        FileOutputStream fout = new FileOutputStream(outputFile);
                        byte[] buffer = new byte[2189];
                        while (is.available() > 0) {
                            int readBytes = is.read(buffer);
                            fout.write(buffer, 0, readBytes);
                        }
                        fout.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) try {
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zipPathData.tmpPath;
    }
