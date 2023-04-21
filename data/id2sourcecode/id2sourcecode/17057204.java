    private void doRenaming(String pathCamera, String text, String mode, boolean chkdate, boolean chkDateDir, boolean chkRename) throws IOException {
        int countProgress = 0;
        String newFileName = null;
        String newPath = null;
        String path;
        String relativePath;
        String dateDir;
        String date = "";
        File oldFile;
        QDir dir = new QDir(pathCamera);
        maxProgres.emit(dir.count());
        QDirIterator dirIt = new QDirIterator(dir);
        dirIt.next();
        dirIt.next();
        while (dirIt.hasNext()) {
            path = dirIt.next();
            oldFile = new File(path);
            relativePath = dir.relativeFilePath(path);
            dateDir = "";
            try {
                if (chkdate) {
                    JpegFileMetadata metadata = new JpegFileMetadata(path);
                    date = metadata.getTimeDate();
                    date = "-" + date;
                }
                if (chkdate && chkDateDir) {
                    JpegFileMetadata metadata = new JpegFileMetadata(path);
                    date = metadata.getOnlyTime();
                    dateDir = QDir.separator() + metadata.getOnlyDate();
                    date = "-" + date;
                }
                if (!chkdate && chkDateDir) {
                    JpegFileMetadata metadata = new JpegFileMetadata(path);
                    dateDir = QDir.separator() + metadata.getOnlyDate();
                }
                if (chkRename) {
                    if (mode.equals(PREFIX)) {
                        newFileName = text + date + relativePath;
                    }
                    if (mode.equals(SUFFIX)) {
                        newFileName = FilenameUtils.getBaseName(path) + text + date + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(path);
                    }
                    if (mode.equals(REPLACE)) {
                        newFileName = text + "[" + countProgress + "]" + date + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(path);
                    }
                } else {
                    newFileName = FilenameUtils.getName(path);
                }
                newPath = ui.leDestination.text() + QDir.separator() + dateDir + QDir.separator() + newFileName;
                File newFile = new File(newPath);
                progres.emit(countProgress++);
                FileUtils.copyFile(oldFile, newFile);
            } catch (Exception e) {
            }
        }
        progres.emit(dir.count());
    }
