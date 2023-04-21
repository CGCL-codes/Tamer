    @Override
    public String execute() {
        try {
            String dataPath = (String) propertyConfigurer.getMergedProperties().get("data.dir");
            if (dataPath == null) dataPath = "data";
            File dataFolder = applicationContext.getResource(dataPath).getFile();
            if (!dataFolder.exists()) {
                log.info("Data folder doesn't exist... Creating " + dataFolder.getAbsolutePath());
                dataFolder.mkdir();
            }
            File theFile = new File(dataFolder, pictureFileName);
            log.debug("Saving picture to " + theFile.getPath());
            FileUtils.copyFile(picture, theFile);
            Picture picture = new Picture(nick, pictureFileName, pictureContentType, this.picture.length());
            picture = pictureService.updatePicture(picture);
        } catch (Exception e) {
            log.error(e, e);
            return INPUT;
        }
        return SUCCESS;
    }
