    public void write(SpantusBundle bundle, File zipFile) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            out.putNextEntry(new ZipEntry(BUNDLE_FILE_SAMPLE));
            readerDao.write(bundle.getReader(), out);
            out.putNextEntry(new ZipEntry(BUNDLE_FILE_MARKER));
            markerDao.write(bundle.getHolder(), out);
            out.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
