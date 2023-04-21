    public static TopicMapRepositoryIF getRepositoryFromClassPath(String resourceName, Map<String, String> environ) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(resourceName);
        if (url == null) throw new OntopiaRuntimeException("Could not find resource '" + resourceName + "' on CLASSPATH.");
        if (environ == null) environ = new HashMap<String, String>(1);
        if (url.getProtocol().equals("file")) {
            String file = url.getFile();
            environ.put("CWD", file.substring(0, file.lastIndexOf('/')));
        } else environ.put("CWD", ".");
        try {
            return createRepository(readSources(new InputSource(url.openStream()), environ));
        } catch (IOException e) {
            throw new OntopiaRuntimeException(e);
        }
    }
