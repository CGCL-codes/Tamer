    public static void writeStringToFile(final String directoryName, final URI uri, final String fileName, final String fileContent, final AbstractAuthenticationToken authenticationToken) throws VFSRetrievalException {
        assert (fileName != null) : "Inappropriate attempt to write a null file name to local directory";
        MyProxyAuthenticationToken myProxyAuthenticationToken = null;
        if (authenticationToken instanceof MyProxyAuthenticationToken) {
            myProxyAuthenticationToken = (MyProxyAuthenticationToken) authenticationToken;
            log.debug("~vfs.writeStringToFile(..) : There's a MyProxy [" + myProxyAuthenticationToken + "]");
        }
        final String processingDirectoryName = retrieveProcessingDirectory(directoryName, uri, myProxyAuthenticationToken);
        final FileSystemOptions fileSystemOptions = FileSystemOptionsUtil.retrieveGridFTPFileSystemOptions(myProxyAuthenticationToken);
        final FileSystemManager fileSystemManager = getFileSystemManager();
        final String processingFileName = processingDirectoryName.concat("/").concat(fileName);
        log.debug("~vfs.writeStringToFile(..) : Instructed to write [" + processingFileName + "]");
        String problemMessage = null;
        OutputStream outputStream = null;
        try {
            log.debug("~vfs.writeStringToFile(..) : Creating the destination file");
            final FileObject destinationFile = fileSystemManager.resolveFile(processingFileName, fileSystemOptions);
            destinationFile.createFile();
            outputStream = destinationFile.getContent().getOutputStream();
            log.debug("~vfs.writeStringToFile(..) : Destination file ready!");
        } catch (FileSystemException fse) {
            fse.printStackTrace();
            if (fse.getCause() != null) {
                problemMessage = fse.getCause().getMessage();
                log.warn("~vfs.writeStringToFile(..) : Could not create local directory file");
            }
        }
        if (outputStream != null) {
            try {
                log.debug("~vfs.writeStringToFile(..) : Preparing the write process");
                outputStream.write(fileContent.getBytes());
                outputStream.flush();
                outputStream.close();
                log.debug("~vfs.writeStringToFile(..) : Content written");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                problemMessage = ioe.getMessage();
                log.warn("~vfs.writeStringToFile(..) : Could not write to local directory file");
            }
        }
        if (problemMessage != null) throw new VFSRetrievalException(MessageKeys.DATA_INVALID, new Object[] { problemMessage });
    }
