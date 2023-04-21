    public static Version getLastVersion(URL url) throws Exception {
        Version result = null;
        InputStream urlStream = null;
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setReadTimeout(10000);
            urlConnection.setUseCaches(true);
            urlStream = urlConnection.getInputStream();
            Properties properties = new Properties();
            properties.load(urlStream);
            result = new Version();
            result.setMajorNumber(Integer.parseInt(properties.getProperty("xtrememp.lastVersion.majorNumber")));
            result.setMinorNumber(Integer.parseInt(properties.getProperty("xtrememp.lastVersion.minorNumber")));
            result.setMicroNumber(Integer.parseInt(properties.getProperty("xtrememp.lastVersion.microNumber")));
            result.setVersionType(Version.VersionType.valueOf(properties.getProperty("xtrememp.lastVersion.versionType")));
            result.setReleaseDate(properties.getProperty("xtrememp.lastVersion.releaseDate"));
            result.setDownloadURL(properties.getProperty("xtrememp.lastVersion.dounloadURL"));
        } finally {
            IOUtils.closeQuietly(urlStream);
        }
        return result;
    }
