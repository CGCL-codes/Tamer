    public void save(OutputStream outStream) throws IOException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        ZipOutputStream zipOut = new ZipOutputStream(outStream);
        synchronized (this.trustedListValues) {
            Properties saveHelperVals = new Properties();
            Properties saveHelperTimestmps = new Properties();
            for (String curKey : trustedListValues.keySet()) {
                String trustVal = String.valueOf(getTrustValue(curKey));
                String timeStmp = dateFormat.format(new Date(getTimeStamp(curKey)));
                saveHelperVals.setProperty(curKey, trustVal);
                saveHelperTimestmps.setProperty(curKey, timeStmp);
            }
            zipOut.putNextEntry(new ZipEntry(TRUSTED_VALUES_NAME));
            saveHelperVals.store(zipOut, "TrustedList. Please do not edit.");
            zipOut.closeEntry();
            zipOut.putNextEntry(new ZipEntry(TRUSTED_TIMES_NAME));
            saveHelperTimestmps.store(zipOut, "TrustedList Timestamps. Please do not edit.");
            zipOut.closeEntry();
            zipOut.finish();
        }
    }
