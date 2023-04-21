    public static synchronized Iterator providers(Class cls) {
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
            while (providerFiles.hasMoreElements()) {
                try {
                    URL url = (URL) providerFiles.nextElement();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String line = reader.readLine();
                    while (line != null) {
                        try {
                            int idx = line.indexOf('#');
                            if (idx != -1) {
                                line = line.substring(0, idx);
                            }
                            line = line.trim();
                            if (line.length() > 0) {
                                Object obj = classLoader.loadClass(line).newInstance();
                                providers.add(obj);
                            }
                        } catch (Exception ex) {
                        }
                        line = reader.readLine();
                    }
                } catch (Exception ex) {
                }
            }
        } catch (IOException ioe) {
        }
        return providers.iterator();
    }
