    private static Collection<Class<?>> load(String filename, String packagePrefix) {
        Collection<Class<?>> services = new ArrayList<Class<?>>();
        try {
            ClassLoader classLoader = DSAnnotatedLoader.class.getClassLoader();
            Enumeration<URL> e = classLoader.getResources(filename);
            while (e.hasMoreElements()) {
                URL url = e.nextElement();
                InputStream is = url.openStream();
                try {
                    BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while (true) {
                        String line = r.readLine();
                        if (line == null) {
                            break;
                        }
                        String name = line.trim();
                        if (name.length() == 0) {
                            continue;
                        }
                        try {
                            Class<?> clz = Class.forName(name, true, classLoader);
                            if (packagePrefix != null && packagePrefix.length() > 0) {
                                if (clz.getPackage().getName().startsWith(packagePrefix)) {
                                    services.add(clz);
                                }
                            } else {
                                services.add(clz);
                            }
                        } catch (ClassNotFoundException ex) {
                        }
                    }
                } finally {
                    is.close();
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while reading {0}", filename);
        }
        return services;
    }
