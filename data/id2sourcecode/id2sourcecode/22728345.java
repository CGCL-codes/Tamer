    public String uploadLogo() {
        if (file != null) {
            for (String suffix : Constants.IMAGE_TYPES) {
                FileUtils.deleteQuietly(dataDir.resourceLogoFile(resource.getShortname(), suffix));
            }
            String type = "jpeg";
            if (fileContentType != null) {
                type = StringUtils.substringAfterLast(fileContentType, "/");
            }
            File logoFile = dataDir.resourceLogoFile(resource.getShortname(), type);
            try {
                FileUtils.copyFile(file, logoFile);
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
        return INPUT;
    }
