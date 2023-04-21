    public String file() throws Exception {
        if (upload == null) {
            return ajaxJsonErrorMessage("请选择上传文件!");
        }
        String allowedUploadFileExtension = getSystemConfig().getAllowedUploadFileExtension().toLowerCase();
        if (StringUtils.isEmpty(allowedUploadFileExtension)) {
            return ajaxJsonErrorMessage("不允许上传文件!");
        }
        String fileExtension = StringUtils.substringAfterLast(uploadFileName, ".").toLowerCase();
        String[] fileExtensionArray = allowedUploadFileExtension.split(SystemConfig.EXTENSION_SEPARATOR);
        if (!ArrayUtils.contains(fileExtensionArray, fileExtension)) {
            return ajaxJsonErrorMessage("只允许上传文件类型: " + allowedUploadFileExtension + "!");
        }
        int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
        if (uploadLimit != 0) {
            if (upload != null && upload.length() > uploadLimit) {
                return ajaxJsonErrorMessage("文件大小超出限制!");
            }
        }
        File uploadFileDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_FILE_DIR));
        if (!uploadFileDir.exists()) {
            uploadFileDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String dateString = simpleDateFormat.format(new Date());
        String uploadFilePath = SystemConfig.UPLOAD_FILE_DIR + dateString + "/" + CommonUtil.getUUID() + "." + fileExtension;
        File file = new File(ServletActionContext.getServletContext().getRealPath(uploadFilePath));
        FileUtils.copyFile(upload, file);
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put("url", ServletActionContext.getServletContext().getContextPath() + uploadFilePath);
        return ajaxJsonErrorMessage("文件大小超出限制!");
    }
