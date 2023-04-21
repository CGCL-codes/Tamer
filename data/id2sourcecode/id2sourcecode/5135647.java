    public static synchronized Iterator providers(Class cls, boolean instantiate) {
        ClassLoader classLoader = cls.getClassLoader();
        String providerFile = "radeox/config/" + cls.getName();
        List providers = services.get(providerFile);
        if (providers != null) {
            return providers.iterator();
        }
        providers = new ArrayList();
        services.put(providerFile, providers);
        try {
            Enumeration providerFiles = classLoader.getResources(providerFile);
            if (providerFiles.hasMoreElements()) {
                while (providerFiles.hasMoreElements()) {
                    URL url = (URL) providerFiles.nextElement();
                    Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
                    if (instantiate) {
                        loadResource(reader, classLoader, providers);
                    } else {
                        loadClasses(reader, classLoader, providers);
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
                    loadResource(reader, classLoader, providers);
                }
            }
        } catch (IOException ioe) {
            System.out.println("Error loading service providers file: " + ioe.getMessage());
        }
        return providers.iterator();
    }
