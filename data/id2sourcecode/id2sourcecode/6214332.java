    private int saveZip(File file) throws IOException, RuntimeException {
        File tempFile = File.createTempFile("tmp" + System.currentTimeMillis(), null, file.getParentFile());
        tempFile.deleteOnExit();
        FileOutputStream stream = new FileOutputStream(tempFile);
        ZipOutputStream zipStream = new ZipOutputStream(stream);
        zipStream.setLevel(Deflater.NO_COMPRESSION);
        zipStream.putNextEntry(new ZipEntry(m_fileable.getNameInZipArchive()));
        List attachements = new ArrayList();
        try {
            m_fileable.writeToStream(zipStream, attachements);
        } catch (Exception e) {
            zipStream.close();
            throw new RuntimeException(e);
        }
        zipAttachments(attachements, zipStream);
        zipStream.close();
        renameTempFile(file, tempFile);
        return SAVE_SUCCEEDED;
    }
