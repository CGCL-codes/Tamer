    public PackageDownloader(String packageName, String packageLocation, LocaleDatabase langpack, String appVersion) {
        this.packageName = packageName;
        this.langpack = langpack;
        try {
            URL url = new URL(packageLocation);
            URLConnection conn = url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestProperty("X-Process-Dashboard-Installer", appVersion);
            totalSize = conn.getContentLength();
            inputStream = conn.getInputStream();
            buildDialog();
            start();
            dialog.show();
        } catch (IOException ioe) {
            inputStream = null;
        }
    }
