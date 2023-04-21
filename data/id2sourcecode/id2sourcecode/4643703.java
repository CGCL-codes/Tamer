    public static void unzip(final String sArchive, final String sDestDir) throws FileError {
        ZipFile odfFile;
        try {
            odfFile = new ZipFile(sArchive);
        } catch (final IOException e) {
            throw new FileError("Can't access ODF file " + sArchive + "!", e);
        }
        for (final Enumeration<? extends ZipEntry> i = odfFile.entries(); i.hasMoreElements(); ) {
            final ZipEntry curEntry = i.nextElement();
            final File curFile = new File(sDestDir, curEntry.getName());
            if (curEntry.isDirectory()) curFile.mkdirs(); else {
                new File(curFile.getParent()).mkdirs();
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    try {
                        inputStream = odfFile.getInputStream(curEntry);
                        outputStream = new FileOutputStream(curFile);
                        for (int j; (j = inputStream.read(FileUtils.BUFFER)) != -1; ) outputStream.write(FileUtils.BUFFER, 0, j);
                    } finally {
                        if (inputStream != null) inputStream.close();
                        if (outputStream != null) outputStream.close();
                    }
                } catch (final IOException e) {
                    throw new FileError("Can't unzip entry " + curEntry.getName() + "!", e);
                }
            }
        }
        DocumentController.getStaticLogger().fine(sArchive + " unzipped to " + sDestDir);
    }
