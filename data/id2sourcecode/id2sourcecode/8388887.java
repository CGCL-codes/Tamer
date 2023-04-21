    public void save_photo() throws Exception {
        String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
        if (photo != null) {
            File saveFile = new File(new File(realPath), photoFileName);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            FileUtils.copyFile(photo, saveFile);
        }
    }
