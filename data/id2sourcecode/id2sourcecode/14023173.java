    public URL[] getPluginPath(URL pluginPathLocation) {
        InputStream input = null;
        if (pluginPathLocation == null) return null;
        try {
            input = pluginPathLocation.openStream();
        } catch (IOException e) {
        }
        if (input == null) try {
            URL url = new URL(PlatformURLBaseConnection.PLATFORM_URL_STRING + PLUGIN_PATH);
            input = url.openStream();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        if (input == null) return null;
        URL[] result = null;
        try {
            try {
                result = readPluginPath(input);
            } finally {
                input.close();
            }
        } catch (IOException e) {
        }
        return result;
    }
