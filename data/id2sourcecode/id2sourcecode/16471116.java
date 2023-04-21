    public void createCashedImage(final CKImageResource imageFile) {
        File cachedFile = imageFile.getCacheFile();
        File srcFile = imageFile.getFile();
        if (cachedFile.exists() && srcFile.lastModified() <= cachedFile.lastModified()) {
            logger.info("Image exists and isn't modified, don't need to render again: ".concat(cachedFile.getAbsolutePath()));
            return;
        }
        if (!cachedFile.getParentFile().exists()) cachedFile.getParentFile().mkdirs();
        try {
            if (!InitializationManager.isRenderingPossible()) {
                logger.warn("Properties for imagemagick aren't set, so I can't render!");
                if (!cachedFile.exists()) {
                    FileUtils.copyFile(srcFile, cachedFile);
                    logger.debug("File successfull copied.");
                }
            } else {
                imageManipulation.resizeImage(srcFile, cachedFile, imageFile.getDimension());
            }
        } catch (Exception e) {
            throw new FatalException("Error while resizing image!", e);
        }
    }
