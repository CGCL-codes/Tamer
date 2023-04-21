    @Override
    public String execute() throws Exception {
        String uploadType = Constant.FILE_TYPE_REQUIREMENT;
        String path = FileAccessUtil.readPropertiesFile("REQUIREMENT_FILE_PATH");
        File dir = new File(path.toString());
        if (!dir.isDirectory()) dir.mkdirs();
        String targetFileName = FileAccessUtil.generateUploadFileName(uploadFileName, uploadType);
        File target = new File(path.toString(), targetFileName);
        FileUtils.copyFile(upload, target);
        return SUCCESS;
    }
