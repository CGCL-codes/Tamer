    public void setResourceContent(String resourceUri, InputStream content, String contentType, String characterEncoding, Date modifiedDate) throws IOException {
        resourceUri = stripMapping(resourceUri);
        Logger.debug(this.getClass(), "setResourceContent");
        String hostName = getHostname(resourceUri);
        String path = getPath(resourceUri);
        String folderName = getFolderName(path);
        String fileName = getFileName(path);
        fileName = deleteSpecialCharacter(fileName);
        Host host = HostFactory.getHostByHostName(hostName);
        Folder folder = FolderFactory.getFolderByPath(folderName, host);
        if (host.getInode() != 0 && folder.getInode() != 0) {
            File destinationFile = FileFactory.getFileByURI(path, host, false);
            java.io.File workingFile = FileFactory.getAssetIOFile(destinationFile);
            DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
            vc.remove(ResourceManager.RESOURCE_TEMPLATE + workingFile.getPath());
            InputStream is = content;
            ByteArrayOutputStream arrayWriter = new ByteArrayOutputStream();
            int read = -1;
            while ((read = is.read()) != -1) {
                arrayWriter.write(read);
            }
            byte[] currentData = arrayWriter.toByteArray();
            File file = FileFactory.getFileByURI(path, host, false);
            file.setSize(currentData.length);
            file.setModDate(modifiedDate);
            InodeFactory.saveInode(file);
            if (currentData != null) {
                FileChannel writeCurrentChannel = new FileOutputStream(workingFile).getChannel();
                writeCurrentChannel.truncate(0);
                ByteBuffer buffer = ByteBuffer.allocate(currentData.length);
                buffer.put(currentData);
                buffer.position(0);
                writeCurrentChannel.write(buffer);
                writeCurrentChannel.force(false);
                writeCurrentChannel.close();
                Logger.debug(this, "WEBDAV fileName:" + fileName + ":" + workingFile.getAbsolutePath());
                if (UtilMethods.isImage(fileName) && workingFile != null) {
                    try {
                        BufferedImage img = javax.imageio.ImageIO.read(workingFile);
                        if (img != null) {
                            int height = img.getHeight();
                            destinationFile.setHeight(height);
                            int width = img.getWidth();
                            destinationFile.setWidth(width);
                        }
                    } catch (Exception ioe) {
                        Logger.error(this.getClass(), ioe.getMessage(), ioe);
                    }
                }
                String folderPath = workingFile.getParentFile().getAbsolutePath();
                Identifier identifier = new Identifier();
                try {
                    identifier = IdentifierCache.getIdentifierFromIdentifierCache(destinationFile);
                } catch (DotHibernateException he) {
                    Logger.error(this, "Cannot load identifier : ", he);
                }
                java.io.File directory = new java.io.File(folderPath);
                java.io.File[] files = directory.listFiles((new FileFactory()).new ThumbnailsFileNamesFilter(identifier));
                for (java.io.File iofile : files) {
                    try {
                        iofile.delete();
                    } catch (SecurityException e) {
                        Logger.error(this, "EditFileAction._saveWorkingFileData(): " + iofile.getName() + " cannot be erased. Please check the file permissions.");
                    } catch (Exception e) {
                        Logger.error(this, "EditFileAction._saveWorkingFileData(): " + e.getMessage());
                    }
                }
            }
        }
    }
