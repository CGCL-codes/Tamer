    private static void copyDefaultLicense() {
        String pathToDefaultLicense = UIPlugin.getDefault().getPluginLocation() + File.separator + "lib" + File.separator + "default.lic";
        File defaultLicenseFile = new File(pathToDefaultLicense);
        if (defaultLicenseFile.exists()) {
            File licenseFile = new File(UIPlugin.getLicencePath());
            try {
                FileUtils.copyFile(defaultLicenseFile, licenseFile);
            } catch (IOException e) {
                UIPlugin.log("Error copying default license", IStatus.ERROR, null);
            }
        } else {
            UIPlugin.log("Unable to locate the default license at: " + defaultLicenseFile, IStatus.ERROR, null);
        }
    }
