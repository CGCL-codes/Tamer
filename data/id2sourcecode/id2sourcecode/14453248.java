    private void _loadProps(String name, String localeKey, boolean useServletContext) {
        Properties props = new Properties();
        try {
            URL url = null;
            if (useServletContext) {
                url = _servletContext.getResource("/WEB-INF/" + name);
            } else {
                ClassLoader classLoader = getClass().getClassLoader();
                url = classLoader.getResource(name);
            }
            if (url != null) {
                InputStream is = url.openStream();
                props.load(is);
                is.close();
                _log.info("Loading " + url);
            }
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
        }
        if (props.size() < 1) {
            return;
        }
        synchronized (messages) {
            Enumeration names = props.keys();
            while (names.hasMoreElements()) {
                String key = (String) names.nextElement();
                messages.put(messageKey(localeKey, key), props.getProperty(key));
            }
        }
    }
