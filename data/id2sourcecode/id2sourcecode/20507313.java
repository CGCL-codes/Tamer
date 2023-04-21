    public static void saveImportedFile(GenericValue project, File file, String filename) throws IOException {
        File attachmentDir = getAttachmentDir(project);
        File attachmentFile = new File(attachmentDir, filename);
        if (attachmentFile.exists()) {
            attachmentFile.delete();
        }
        FileUtils.copyFile(file, attachmentFile);
    }
