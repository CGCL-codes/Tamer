    public void saveSongbookAsZip(Songbook songbook, File songbookfile) throws IOException {
        FileOutputStream fos = new FileOutputStream(songbookfile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.setComment("LeadSheetMaker Songbook file. Saved on " + new Date().toString() + ".");
        ZipEntry e = new ZipEntry(IOConstants.METADATA_DIRECTORY + "/" + IOConstants.INDEX_FILE_NAME);
        zos.putNextEntry(e);
        writeIndex(songbook, zos);
        zos.closeEntry();
        ZipEntry ple = new ZipEntry(IOConstants.METADATA_DIRECTORY + "/" + IOConstants.PLAYLISTS_FILE_NAME);
        zos.putNextEntry(ple);
        writePlaylists(songbook, zos);
        zos.closeEntry();
        for (Song s : songbook.getSongs()) {
            e = new ZipEntry(convertToFileName(s));
            try {
                zos.putNextEntry(e);
            } catch (ZipException e1) {
                e = new ZipEntry(convertToFileName(s) + System.currentTimeMillis());
                zos.putNextEntry(e);
            }
            saveSong(s, zos, false);
            zos.closeEntry();
        }
        IOUtils.closeQuietly(zos);
    }
