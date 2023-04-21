    public void initialize(Set<FileDescriptor> fileNames) {
        pagesByViewId = Collections.synchronizedMap(new HashMap<String, Page>());
        pageStacksByViewId = Collections.synchronizedMap(new HashMap<String, List<Page>>());
        conversations = Collections.synchronizedMap(new HashMap<String, ConversationIdParameter>());
        for (String resource : resources) {
            InputStream stream = ResourceLoader.instance().getResourceAsStream(resource);
            if (stream == null) {
                log.debug("no pages.xml file found: " + resource);
            } else {
                log.debug("reading pages.xml file: " + resource);
                try {
                    parse(stream);
                } finally {
                    Resources.closeStream(stream);
                }
            }
        }
        Enumeration<URL> resources;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/pages.xml");
        } catch (IOException ioe) {
            throw new RuntimeException("error scanning META-INF/pages.xml files", ioe);
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try {
                log.debug("reading " + url);
                parse(url.openStream());
            } catch (Exception e) {
                throw new RuntimeException("error while reading " + url, e);
            }
        }
        if (fileNames != null) {
            parsePages(fileNames);
        }
    }
