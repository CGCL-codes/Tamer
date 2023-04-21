    @Override
    protected int createFile(final int utc, final String extra_ext, final boolean createNewFile) {
        String zipFileName;
        String zipEntryFileName;
        setTrackName(extra_ext);
        zipFileName = filenameBuilder.getOutputFileName(basename, utc, ".kmz", extra_ext).getPath();
        zipEntryFileName = basename + extra_ext + ".kml";
        int l;
        l = zipEntryFileName.lastIndexOf('/');
        if (l > 0) {
            zipEntryFileName = zipEntryFileName.substring(l + 1);
        }
        l = zipEntryFileName.lastIndexOf('\\');
        if (l > 0) {
            zipEntryFileName = zipEntryFileName.substring(l + 1);
        }
        l = zipEntryFileName.lastIndexOf(':');
        if (l > 0) {
            zipEntryFileName = zipEntryFileName.substring(l + 1);
        }
        int error = BT747Constants.NO_ERROR;
        try {
            if (createNewFile) {
                final File tmpFile = new File(zipFileName);
                if (tmpFile.exists()) {
                    tmpFile.delete();
                }
            }
        } catch (final Exception e) {
            Generic.debug("File deletion", e);
        }
        try {
            currentZipStream = null;
            if (createNewFile) {
                final FileOutputStream fos = new FileOutputStream(zipFileName, false);
                final BufferedOutputStream bos = new BufferedOutputStream(fos);
                currentZipStream = new ZipOutputStream(bos);
                final ZipEntry e = new ZipEntry(zipEntryFileName);
                currentZipStream.putNextEntry(e);
                zips.put(zipFileName, currentZipStream);
            } else {
                if (zips == null) {
                    Generic.debug("Zip name is null ");
                } else {
                    currentZipStream = (ZipOutputStream) zips.get(zipFileName);
                    if (currentZipStream == null) {
                        Generic.debug("Could not find " + zipFileName + " zip stream.");
                    }
                }
            }
        } catch (final Exception e) {
            Generic.debug("Zip Entry Creation", e);
        }
        return error;
    }
