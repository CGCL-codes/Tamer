    public void save(IItemProgressMonitor monitor) throws ZipException, IOException {
        if (m_strFilename == null || "".equals(m_strFilename)) throw new FileNotFoundException();
        deleteMedia();
        File fileTmp = File.createTempFile("librarian-save", FileStorageUtil.LIBRARIAN_FILE_EXTENSION);
        File file = new File(m_strFilename);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileTmp));
        out.setLevel(Deflater.DEFAULT_COMPRESSION);
        out.putNextEntry(new ZipEntry("settings.xml"));
        m_settings.toStream(out);
        out.closeEntry();
        m_metadata.storeIcons(out);
        out.putNextEntry(new ZipEntry("db.xml"));
        if (toStream(file, out, monitor)) {
            out.closeEntry();
            out.close();
            m_bIsDirty = false;
            FileUtil.copyFile(fileTmp, file);
        } else {
            out.close();
            fileTmp.delete();
        }
    }
