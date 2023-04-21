    private static OutputStream retrieveOutputStream(final String fileName, final StringBuffer problemMessage) {
        log.debug("~retrieveOutputStream(String) : Retrieving output stream for [" + fileName + "]");
        OutputStream outputStream = null;
        final FileSystemManager fileSystemManager = getFileSystemManager();
        try {
            log.debug("~vfs.writeStringToFile(..) : Creating the destination file");
            final FileObject destinationFile = fileSystemManager.resolveFile(fileName);
            destinationFile.createFile();
            outputStream = destinationFile.getContent().getOutputStream();
            log.debug("~vfs.writeStringToFile(..) : Destination file ready!");
        } catch (FileSystemException fse) {
            fse.printStackTrace();
            if (fse.getCause() != null) {
                log.warn("~vfs.writeStringToFile(..) : Could not create local directory file");
                problemMessage.append(fse.getMessage());
            }
        }
        return outputStream;
    }
