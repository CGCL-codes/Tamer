    private void initKeys() {
        try {
            if (m_Folder != null) {
                m_Properties.load(new FileInputStream(m_Folder + "/messages_" + getLanguage() + "_" + getCountry() + ".properties"));
            } else {
                java.net.URL url = getClass().getResource(m_StringFile);
                m_Properties.load(url.openStream());
            }
        } catch (Exception e) {
            Logger.getInstance().logException(e, false);
        }
    }
