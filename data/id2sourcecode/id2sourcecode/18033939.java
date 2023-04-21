    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "friendLink.name", message = "友情链接名称不允许为空!"), @RequiredStringValidator(fieldName = "friendLink.url", message = "链接地址不允许为空!") }, requiredFields = { @RequiredFieldValidator(fieldName = "friendLink.orderList", message = "排序不允许为空!") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "friendLink.orderList", min = "0", message = "排序必须为零或正整数!") })
    @InputConfig(resultName = "error")
    public String save() throws Exception {
        String allowedUploadImageExtension = getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
        if (StringUtils.isEmpty(allowedUploadImageExtension)) {
            addActionError("不允许上传图片文件!");
            return ERROR;
        }
        String[] imageExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
        if (logo != null) {
            String logoExtension = StringUtils.substringAfterLast(logoFileName, ".").toLowerCase();
            if (!ArrayUtils.contains(imageExtensionArray, logoExtension)) {
                addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                return ERROR;
            }
        }
        int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
        if (uploadLimit != 0) {
            if (logo != null && logo.length() > uploadLimit) {
                addActionError("Logo文件大小超出限制!");
                return ERROR;
            }
        }
        if (logo != null) {
            String logoFilePath = SystemConfig.UPLOAD_IMAGE_DIR + CommonUtil.getUUID() + "." + StringUtils.substringAfterLast(logoFileName, ".").toLowerCase();
            File logoFile = new File(ServletActionContext.getServletContext().getRealPath(logoFilePath));
            FileUtils.copyFile(logo, logoFile);
            friendLink.setLogo(logoFilePath);
        }
        friendLinkService.save(friendLink);
        redirectionUrl = "friend_link!list.action";
        return SUCCESS;
    }
