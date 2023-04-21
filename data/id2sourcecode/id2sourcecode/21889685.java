    private void loadNewVersionFromURL(boolean allowToGatherUsageStatsEnabled) {
        lastVersionCachedDate = new Date();
        try {
            URL url = getURL(allowToGatherUsageStatsEnabled);
            String lastVersion = (new BufferedReader(new InputStreamReader(url.openStream()))).readLine();
            if (projectVersion != null && lastVersion != null) {
                newVersionCached = !projectVersion.equals(lastVersion);
            }
        } catch (MalformedURLException e) {
            LOG.warn("Problems generating URL to check LibrePlan version. MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            LOG.info("Could not check LibrePlan version information from " + LIBREPLAN_VERSION_URL + ". IOException: " + e.getMessage());
        }
    }
