    public String execute() throws Exception {
        String path = FileAccessUtil.readPropertiesFile("CV_FILE_PATH");
        String uploadType = Constant.FILE_TYPE_CV;
        StringBuffer[] cvs = new StringBuffer[2];
        if (uploads.get(0) != null) {
            cvs[0] = new StringBuffer("");
            cvs[0].append("cn_" + FileAccessUtil.generateUploadFileName(fileNames.get(0), uploadType));
        }
        if (uploads.get(1) != null) {
            cvs[1] = new StringBuffer("");
            cvs[1].append("en_" + FileAccessUtil.generateUploadFileName(fileNames.get(1), uploadType));
        }
        System.out.println(cvs[0].toString());
        System.out.println(cvs[1].toString());
        File dir = new File(path);
        if (!dir.isDirectory()) dir.mkdirs();
        File target_cn = new File(path, cvs[0].toString());
        File target_en = new File(path, cvs[1].toString());
        FileUtils.copyFile(uploads.get(0), target_cn);
        FileUtils.copyFile(uploads.get(1), target_en);
        return SUCCESS;
    }
