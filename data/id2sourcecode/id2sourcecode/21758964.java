    public static synchronized Iterator providers(Class cls, int providerKind) {
        ClassLoader classLoader = cls.getClassLoader();
        String providerFile = "META-INF/services/" + cls.getName();
        List providers = (List) services.get(providerFile);
        if (providers != null) {
            return providers.iterator();
        }
        providers = new ArrayList();
        services.put(providerFile, providers);
        try {
            Enumeration providerFiles = classLoader.getResources(providerFile);
            if (providerFiles.hasMoreElements()) {
                while (providerFiles.hasMoreElements()) {
                    try {
                        URL url = (URL) providerFiles.nextElement();
                        Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
                        switch(providerKind) {
                            case Service.RESOURCE:
                                loadResources(reader, classLoader, providers);
                                break;
                            case Service.CLASS:
                                loadClasses(reader, classLoader, providers);
                                break;
                            case Service.INSTANCE:
                                loadInstances(reader, classLoader, providers);
                                break;
                        }
                    } catch (Exception ex) {
                    }
                }
            } else {
                InputStream is = classLoader.getResourceAsStream(providerFile);
                if (is == null) {
                    providerFile = providerFile.substring(providerFile.lastIndexOf('.') + 1);
                    is = classLoader.getResourceAsStream(providerFile);
                }
                if (is != null) {
                    Reader reader = new InputStreamReader(is, "UTF-8");
                    loadInstances(reader, classLoader, providers);
                }
            }
        } catch (IOException ioe) {
        }
        return providers.iterator();
    }
