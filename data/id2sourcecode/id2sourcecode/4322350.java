    public boolean UploadLogo() {
        try {
            String filePath = servletRequest.getRealPath("/") + "upload\\";
            File fileToCreate = new File(filePath, this.userLogoFileName);
            FileUtils.copyFile(this.userLogo, fileToCreate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
