    private OxygenResourceBundle(String basename, Locale locale) throws Exception {
        String bundleName = basename;
        String localeAsStr = locale.toString();
        if (!StringUtils.isBlank(localeAsStr)) {
            bundleName = bundleName + "_" + localeAsStr;
        }
        String resName = bundleName.replace('.', '/') + ".properties";
        for (Enumeration enum0 = Thread.currentThread().getContextClassLoader().getResources(resName); enum0.hasMoreElements(); ) {
            URL url = (URL) enum0.nextElement();
            InputStream is = url.openStream();
            try {
                Properties p = new Properties();
                p.load(is);
                map.putAll(p);
            } finally {
                CloseUtils.close(is);
            }
        }
    }
