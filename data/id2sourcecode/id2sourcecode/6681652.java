    public void step2() throws IOException {
        if (logger.isInfoEnabled()) logger.info("step2[]");
        File licenseDir = new File(ServletActionContext.getServletContext().getRealPath("license"));
        if (!licenseDir.exists()) licenseDir.mkdir();
        File tempLicense = new File(licenseDir.getAbsoluteFile() + File.separator + "license_temp.xml");
        File licenseFile = new File(licenseDir.getAbsoluteFile() + File.separator + "license.xml");
        if (licenseFile.exists()) licenseFile.delete();
        FileUtils.copyFile(tempLicense, licenseFile);
        FileUtils.deleteQuietly(tempLicense);
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            logger.error(e, e);
        }
    }
