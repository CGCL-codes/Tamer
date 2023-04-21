    public ZipFile getCachedZip(String urlName) {
        CryptoFunctions cfunctions = new CryptoFunctions();
        cfunctions.initialize(m_application);
        String cacheName = cfunctions.encrypt(urlName);
        cacheName = cacheName.substring(cacheName.length() - 20, cacheName.length());
        cacheName = cacheName.replace('/', '_');
        String cacheFilePath = m_application.getApplicationContext().getProperty("cache_FilePath");
        String cacheFileName = cacheFilePath + cacheName;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(cacheFileName), ZipFile.OPEN_READ);
            return zipFile;
        } catch (Exception e) {
        }
        URL url;
        try {
            url = new URL(urlName);
        } catch (MalformedURLException e) {
            m_application.getLogger().logError(8, "getURLWithCache: Malformed URL: " + urlName);
            return null;
        }
        HttpURLConnection urlConnection;
        InputStream is;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(true);
            urlConnection.setUseCaches(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                m_application.getLogger().logError(8, "getURLWithCache: HTTP Connection failed with status: " + statusCode);
                return null;
            }
            is = urlConnection.getInputStream();
            FileOutputStream out = new FileOutputStream(cacheFileName);
            byte[] buffer = new byte[1024];
            int len = is.read(buffer);
            while (len >= 0) {
                out.write(buffer, 0, len);
                len = is.read(buffer);
            }
            is.close();
            out.close();
            m_application.getLogger().logMessage(0, "getCachedURL: wrote file to cache: " + cacheName);
        } catch (Exception e) {
            m_application.getLogger().logError(8, "getURLWithCache: HTTP Connection failed with exception: " + e);
            return null;
        }
        try {
            zipFile = new ZipFile(new File(cacheFileName), ZipFile.OPEN_READ);
            return zipFile;
        } catch (Exception e) {
            return null;
        }
    }
