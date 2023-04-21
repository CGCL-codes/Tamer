    private void storePhoto() throws Exception {
        LOG.trace("Filename: " + this.photoFilename);
        if ((this.photoFile != null) && (this.photoFilename != null) && (this.photoFilename.trim().length() > 0)) {
            try {
                String imageDir = "/resources/photos/";
                ServletContext context = ServletActionContext.getServletContext();
                String filesystemImageDir = context.getRealPath(imageDir);
                LOG.trace("Filesystem image dir is " + filesystemImageDir);
                new File(filesystemImageDir).mkdirs();
                String pathToSavePhotoAt = filesystemImageDir + "/" + this.photoFilename;
                LOG.trace("pathToSavePhotoAt " + pathToSavePhotoAt);
                File newPhotoFile = new File(pathToSavePhotoAt);
                FileUtils.copyFile(this.photoFile, newPhotoFile);
                contact.setPhotoUri(this.photoFilename);
                String msg = "Successfully saved file at " + newPhotoFile.getPath();
                LOG.debug(msg);
                addActionMessage(msg);
            } catch (Exception e) {
                throw new Exception("Could not upload photo " + this.photoFilename + " because of exception " + e.getMessage(), e);
            }
        }
    }
