    private boolean checkNetworkAvailability(Proxy proxy) {
        try {
            LOGGER.debug("Test network with " + REACH_TEST_URL);
            lastCheckTime = System.currentTimeMillis();
            URL urlForTest = new URL(REACH_TEST_URL);
            URLConnection testConnection = urlForTest.openConnection(proxy);
            testConnection.connect();
            lastCheckResult = true;
        } catch (Exception e) {
            LOGGER.error(e);
            lastCheckResult = false;
        }
        return lastCheckResult;
    }
