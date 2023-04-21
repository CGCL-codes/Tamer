    private void extractZip(String zipFile, String destDir) throws IOException, URISyntaxException {
        final File createTempFile = File.createTempFile("templatedb", "zip");
        OutputStream os = new FileOutputStream(createTempFile);
        InputStream is = getClass().getResourceAsStream(zipFile);
        FileUtils.copyFile(is, os);
        final File fileDestDir = new File(destDir);
        assert !fileDestDir.exists() : "Target must not exist";
        fileDestDir.mkdir();
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(createTempFile);
        java.util.Enumeration _enum = zip.entries();
        while (_enum.hasMoreElements()) {
            java.util.zip.ZipEntry file = (java.util.zip.ZipEntry) _enum.nextElement();
            java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
            if (file.isDirectory()) {
                f.mkdir();
                continue;
            } else {
                InputStream fis = zip.getInputStream(file);
                OutputStream fos = new java.io.FileOutputStream(f);
                FileUtils.copyFile(fis, fos);
            }
        }
    }
