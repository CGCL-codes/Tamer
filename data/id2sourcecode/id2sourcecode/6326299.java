    public String image() throws Exception {
        if (upload == null) {
            return ajaxJsonErrorMessage("请选择上传文件!");
        }
        String allowedUploadImageExtension = getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
        if (StringUtils.isEmpty(allowedUploadImageExtension)) {
            return ajaxJsonErrorMessage("不允许上传图片文件!");
        }
        String imageExtension = StringUtils.substringAfterLast(uploadFileName, ".").toLowerCase();
        String[] imageExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
        if (!ArrayUtils.contains(imageExtensionArray, imageExtension)) {
            return ajaxJsonErrorMessage("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
        }
        int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
        if (uploadLimit != 0) {
            if (upload != null && upload.length() > uploadLimit) {
                return ajaxJsonErrorMessage("文件大小超出限制!");
            }
        }
        File uploadImageDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_IMAGE_DIR));
        if (!uploadImageDir.exists()) {
            uploadImageDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String dateString = simpleDateFormat.format(new Date());
        String uploadImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + CommonUtil.getUUID() + "." + imageExtension;
        File file = new File(ServletActionContext.getServletContext().getRealPath(uploadImagePath));
        FileUtils.copyFile(upload, file);
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put("url", ServletActionContext.getServletContext().getContextPath() + uploadImagePath);
        return ajaxJson(jsonMap);
    }
