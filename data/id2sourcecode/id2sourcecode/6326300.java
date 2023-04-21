    public String media() throws Exception {
        if (upload == null) {
            return ajaxJsonErrorMessage("请选择上传文件!");
        }
        String allowedUploadMediaExtension = getSystemConfig().getAllowedUploadMediaExtension().toLowerCase();
        if (StringUtils.isEmpty(allowedUploadMediaExtension)) {
            return ajaxJsonErrorMessage("不允许上传媒体文件!");
        }
        String mediaExtension = StringUtils.substringAfterLast(uploadFileName, ".").toLowerCase();
        String[] mediaExtensionArray = allowedUploadMediaExtension.split(SystemConfig.EXTENSION_SEPARATOR);
        if (!ArrayUtils.contains(mediaExtensionArray, mediaExtension)) {
            return ajaxJsonErrorMessage("只允许上传媒体文件类型: " + allowedUploadMediaExtension + "!");
        }
        int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
        if (uploadLimit != 0) {
            if (upload != null && upload.length() > uploadLimit) {
                return ajaxJsonErrorMessage("文件大小超出限制!");
            }
        }
        File uploadMediaDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_MEDIA_DIR));
        if (!uploadMediaDir.exists()) {
            uploadMediaDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String dateString = simpleDateFormat.format(new Date());
        String uploadMediaPath = SystemConfig.UPLOAD_MEDIA_DIR + dateString + "/" + CommonUtil.getUUID() + "." + mediaExtension;
        File file = new File(ServletActionContext.getServletContext().getRealPath(uploadMediaPath));
        FileUtils.copyFile(upload, file);
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put("url", ServletActionContext.getServletContext().getContextPath() + uploadMediaPath);
        return ajaxJson(jsonMap);
    }
