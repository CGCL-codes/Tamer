    public RegistryLoaderIni(String[] urls, ServerDriverFactory serverDriverFactory, boolean includeDefaultGroups) throws IOException {
        this.serverDriverFactory = serverDriverFactory;
        URL[] realURLs = new URL[includeDefaultGroups ? urls.length + 1 : urls.length];
        for (int i = 0; i < urls.length; i++) {
            realURLs[i] = PropertiesUtil.stringToURL(urls[i]);
            if (realURLs[i] == null) throw new IllegalArgumentException("Can not find registry file:" + urls[i]);
        }
        if (includeDefaultGroups) {
            String url = "resources/data/default_registry_groups.ini";
            realURLs[urls.length] = PropertiesUtil.stringToURL(url);
            if (realURLs[urls.length] == null) throw new IllegalArgumentException("Can not find registry file:" + url);
        }
        StringBuffer srcDescription = new StringBuffer();
        ByteArrayOutputStream config = new ByteArrayOutputStream();
        for (int i = 0; i < realURLs.length; i++) {
            if (i > 0) srcDescription.append(",");
            srcDescription.append(realURLs[i].toExternalForm());
            InputStream is = new BufferedInputStream(realURLs[i].openStream());
            int b = 0;
            while ((b = is.read()) != -1) config.write(b);
            is.close();
            config.write('\n');
        }
        InputStream is = new ByteArrayInputStream(config.toByteArray());
        readConfig(is, srcDescription.toString());
        is.close();
        this.serverDriverFactory = serverDriverFactory;
    }
