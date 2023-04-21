    private void initComponentsFromXmlDocuments() {
        Enumeration<URL> resources;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/components.xml");
        } catch (IOException ioe) {
            throw new RuntimeException("error scanning META-INF/components.xml files", ioe);
        }
        Properties replacements = getReplacements();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                log.info("reading " + url);
                installComponentsFromXmlElements(XML.getRootElement(url.openStream()), replacements);
            } catch (Exception e) {
                throw new RuntimeException("error while reading " + url, e);
            }
        }
    }
