    UpdateInformation(URL url) throws UnknownHostException, IOException {
        HttpURLConnection ct = (HttpURLConnection) url.openConnection(Preferences.INSTANCE.getHttpProxy());
        errorCode = ct.getResponseCode();
        if (errorCode == HttpURLConnection.HTTP_OK) {
            Properties p = new Properties();
            String encoding = ct.getContentEncoding();
            InputStream in = ct.getInputStream();
            p.load(new InputStreamReader(in, encoding));
            String serialNumber = p.getProperty("serialNumber");
            YapbamState.INSTANCE.put(VersionManager.SERIAL_NUMBER, serialNumber);
            lastestRelease = new ReleaseInfo(p.getProperty("lastestRelease"));
            updateURL = new URL(p.getProperty("updateURL"));
            autoUpdateURL = new URL(p.getProperty("autoUpdateURL"));
            autoUpdateCheckSum = CheckSum.toString(CheckSum.toBytes(p.getProperty("autoUpdateCHKSUM")));
            autoUpdateSize = Long.parseLong(p.getProperty("autoUpdateSize"));
            autoUpdaterURL = new URL(p.getProperty("autoUpdateUpdaterURL"));
            autoUpdaterCheckSum = CheckSum.toString(CheckSum.toBytes(p.getProperty("autoUpdateUpdaterCHKSUM")));
            autoUpdaterSize = Long.parseLong(p.getProperty("autoUpdateUpdaterSize"));
        }
    }
