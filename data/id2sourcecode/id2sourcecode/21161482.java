    public static void unZip(File zippedFile, File destDir) throws IOException, ZipException {
        if (zippedFile != null && destDir != null) {
            ZipFile zipFile = new ZipFile(zippedFile);
            Enumeration zipEntries = zipFile.entries();
            ZipEntry currentEntry;
            File currentFile;
            BufferedOutputStream out;
            InputStream in;
            while (zipEntries.hasMoreElements()) {
                currentEntry = (ZipEntry) zipEntries.nextElement();
                if (!currentEntry.isDirectory()) {
                    currentFile = new File(destDir.getAbsolutePath() + File.separator + currentEntry.getName());
                    new File(currentFile.getParent()).mkdirs();
                    out = new BufferedOutputStream(new FileOutputStream(currentFile));
                    in = zipFile.getInputStream(currentEntry);
                    for (int currentByte = in.read(); currentByte != -1; currentByte = in.read()) out.write(currentByte);
                    in.close();
                    out.close();
                }
            }
        } else throw new ZipException(MAIN_RESOURCE_BUNDLE.getString("default.ZipException.text"));
    }
