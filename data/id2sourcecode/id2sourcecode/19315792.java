    private void echoVersion() throws Exception, MalformedURLException, DocumentException, IOException {
        String localVersion = Helper.getVersion();
        URL url = new URL(urlString);
        versionService.load(new InputStreamReader(url.openStream()));
        String newVersion = versionService.getValue("version");
        if (Float.valueOf(newVersion) > Float.valueOf(localVersion)) {
            m_messageHandler.info(m_mainView, i18n.getString("headline_new_version_available"), i18n.getString("message_new_version_available"));
        }
    }
