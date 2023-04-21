    public void copyImage(File srcImage, String scale) {
        Integer width = new ImageTag().getScale(scale);
        if (srcImage.exists()) {
            File destinationPath = new File(this.imagePath, FilenameUtils.getName(srcImage.getPath()));
            if (!destinationPath.exists()) {
                try {
                    if (width == null && Utilities.getImageWidth(srcImage) > PAGE_WIDTH) {
                        LOG.warn("Image: '" + srcImage.getPath() + "' is too big for the page");
                    }
                    FileUtils.copyFileToDirectory(srcImage, this.imagePath);
                } catch (IOException e) {
                    LOG.warn("Error while copying " + srcImage.getPath() + ":\n" + "\t\t" + e.getMessage());
                    throw new TubainaException("Couldn't copy image", e);
                }
            } else {
                LOG.warn("Error while copying '" + srcImage.getPath() + "':\n" + "\t\tDestination image '" + destinationPath.getPath() + "' already exists");
            }
        } else {
            LOG.warn("Image: '" + srcImage.getPath() + "' doesn't exists");
            throw new TubainaException("Image doesn't exists");
        }
    }
