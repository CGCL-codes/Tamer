    protected static List<InputSource> getBetwixtConfig() {
        try {
            List<InputSource> list = new ArrayList<InputSource>();
            Enumeration<URL> mappings = Thread.currentThread().getContextClassLoader().getResources("betwixt-config.xml");
            while (mappings.hasMoreElements()) {
                URL url = (URL) mappings.nextElement();
                InputSource is = new InputSource(url.openStream());
                if (is != null) {
                    list.add(is);
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
