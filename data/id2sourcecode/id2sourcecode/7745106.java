    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "brand.name", message = "品牌名称不允许为空!") }, urls = { @UrlValidator(fieldName = "brand.url", message = "网址格式错误!") }, requiredFields = { @RequiredFieldValidator(fieldName = "brand.orderList", message = "排序不允许为空!") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "brand.orderList", min = "0", message = "排序必须为零或正整数!") })
    @InputConfig(resultName = "error")
    public String update() throws Exception {
        Brand persistent = brandService.load(id);
        if (logo != null) {
            String allowedUploadImageExtension = getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
            if (StringUtils.isEmpty(allowedUploadImageExtension)) {
                addActionError("不允许上传图片文件!");
                return ERROR;
            }
            String[] imageExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
            String logoExtension = StringUtils.substringAfterLast(logoFileName, ".").toLowerCase();
            if (!ArrayUtils.contains(imageExtensionArray, logoExtension)) {
                addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                return ERROR;
            }
            int uploadLimit = getSystemConfig().getUploadLimit() * 1024;
            if (uploadLimit != 0 && logo.length() > uploadLimit) {
                addActionError("Logo文件大小超出限制!");
                return ERROR;
            }
            File uploadImageDir = new File(ServletActionContext.getServletContext().getRealPath(SystemConfig.UPLOAD_IMAGE_DIR));
            if (!uploadImageDir.exists()) {
                uploadImageDir.mkdirs();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
            String dateString = simpleDateFormat.format(new Date());
            String uploadImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + CommonUtil.getUUID() + "." + logoExtension;
            File file = new File(ServletActionContext.getServletContext().getRealPath(uploadImagePath));
            FileUtils.copyFile(logo, file);
            persistent.setLogo(uploadImagePath);
        }
        BeanUtils.copyProperties(brand, persistent, new String[] { "id", "createDate", "modifyDate", "logo", "productSet" });
        brandService.update(persistent);
        redirectionUrl = "brand!list.action";
        return SUCCESS;
    }
