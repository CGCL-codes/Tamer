    public boolean configure() throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(createUri("configure"));
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setUseCaches(false);
                conn.connect();
            } catch (Exception ex) {
                AgentUtils.printMessage("Cannot connect to " + url.toString() + ": " + ex.getMessage());
                return false;
            }
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }
            ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(conn.getInputStream()));
            ConfigMessage configMessage = (ConfigMessage) ois.readObject();
            ConfigManager.getInstance().getConfig().setDebug(configMessage.isDebug());
            if (configMessage.getStabilityPeriod() < 1440) {
                ConfigManager.getInstance().getConfig().setStabilityPeriod(configMessage.getStabilityPeriod());
            }
            if (ConfigManager.getInstance().getConfig().isDebug()) {
                JavaConfigManager.getInstance().getConfig().setTransactionThreshold(0);
                JavaConfigManager.getInstance().getConfig().setFunctionThreshold(0);
            }
            ConfigManager.getInstance().getConfig().setDisabled(configMessage.isDisabled());
            return true;
        } catch (Exception e) {
            AgentUtils.printStackTrace(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }
