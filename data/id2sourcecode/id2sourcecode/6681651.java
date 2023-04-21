    public byte[] step1(UploadBean uploadBean) throws IOException, ValidateException {
        if (logger.isInfoEnabled()) logger.info("step1[" + uploadBean + "]");
        byte[] xml = null;
        try {
            licenseUploadUtil.setNullable(false);
            licenseUploadUtil.validate(uploadBean);
            if (uploadBean != null && uploadBean.getMyFile() != null) {
                xml = FileUtils.readFileToByteArray(uploadBean.getMyFile());
                File licenseDir = new File(ServletActionContext.getServletContext().getRealPath("license"));
                if (!licenseDir.exists()) licenseDir.mkdir();
                File licenseFile = new File(licenseDir.getAbsoluteFile() + File.separator + "license_temp.xml");
                FileUtils.copyFile(uploadBean.getMyFile(), licenseFile);
            }
        } catch (ValidateException e) {
            throw e;
        }
        return xml;
    }
