    private String createZip(String baseFileName) {
        File reportResourceDirectory = new File(baseFileName + "_files");
        String zipFile = baseFileName + ".zip";
        if (reportResourceDirectory.exists() && reportResourceDirectory.isDirectory()) {
            ZipOutputStream reportArchive;
            try {
                reportArchive = new ZipOutputStream(new FileOutputStream(zipFile));
                addFileToArchive(reportArchive, baseFileName);
                reportArchive.putNextEntry(new ZipEntry(baseFileName));
                for (String file : Arrays.asList(reportResourceDirectory.list())) {
                    addFileToArchive(reportArchive, file);
                }
                reportArchive.close();
            } catch (final Exception e) {
                LogUtils.warnf(this, e, "unable to create %s", zipFile);
            }
        }
        return zipFile;
    }
