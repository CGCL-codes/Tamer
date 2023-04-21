    protected void activate(ComponentContext context) {
        if (this.logService != null) {
            this.logService.log(LogService.LOG_INFO, "Updating TemperatureSensorService Configuration");
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] resultingBytes = md5.digest(this.uuId.getBytes());
            this.merId = Base64.encodeBase64URLSafeString(resultingBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        bundleSymbolicName = context.getBundleContext().getBundle().getSymbolicName();
        Preferences pref = this.prefService.getSystemPreferences();
        bundlePrefs = pref.node(bundleSymbolicName);
        this.setAverage(bundlePrefs.getDouble(TemperatureSensorServiceImpl.AVG_PROPERTY_NAME, this.average));
        this.setRange(bundlePrefs.getDouble(TemperatureSensorServiceImpl.RANGE_PROPERTY_NAME, this.range));
        this.setThreshold(bundlePrefs.getDouble(TemperatureSensorServiceImpl.THRESHOLD_PROPERTY_NAME, this.threshold));
    }
