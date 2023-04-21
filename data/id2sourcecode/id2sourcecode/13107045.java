    public boolean UploadImage() {
        try {
            String filePath = servletRequest.getRealPath("/") + "images\\";
            File fileToCreate = new File(filePath, this.itemImageFileName);
            FileUtils.copyFile(this.itemImage, fileToCreate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
