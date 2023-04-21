    private void handleAttachments(File outputDir) throws IOException {
        if ((getDataCategory().equals(DataCategory.Event) || getDataCategory().equals(DataCategory.OnAndNonRoad)) && StringUtils.isNotBlank(getAttachmentFolderPath())) {
            File attachDir = new File(getAttachmentFolderPath());
            if (attachDir.canRead() && attachDir.isDirectory()) {
                AttachmentDao dao = new AttachmentDao(getPluginDataSource());
                String fileName = dao.getFilename(getDataCategory(), getEmissionYear());
                if (StringUtils.isNotBlank(fileName)) {
                    File attachment = new File(FilenameUtils.concat(attachDir.getAbsolutePath(), fileName));
                    FileUtils.copyFileToDirectory(attachment, outputDir, true);
                    if (getDataCategory().equals(DataCategory.OnAndNonRoad)) {
                        setNdcDataFileName(fileName);
                    }
                }
            }
        }
    }
