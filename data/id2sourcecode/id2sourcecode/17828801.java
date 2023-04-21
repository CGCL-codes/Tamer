    private String handlePropertiesImport(URL url) {
        if (m_initializedModules.containsKey(url)) {
            return url.getRef();
        }
        try {
            java.util.Properties props = new java.util.Properties();
            props.load(url.openStream());
            m_initializedModules.put(url, null);
            String fragment = url.getRef();
            if (fragment == null) {
                addProperties("", props);
            } else {
                addProperties(fragment + ".", props);
            }
            return fragment;
        } catch (final java.io.IOException ex) {
            final String msg = "Unable to read properties file from URL \"" + url + "\"";
            throw new CascadingRuntimeException(msg + " (" + ex + ")", ex);
        }
    }
