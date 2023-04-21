    private ResourceLoader _getResourceLoader(HttpServletRequest request) {
        final String servletPath = request.getServletPath();
        ResourceLoader loader = _loaders.get(servletPath);
        if (loader == null) {
            try {
                String key = "META-INF/servlets/resources" + servletPath + ".resources";
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                URL url = cl.getResource(key);
                if (url != null) {
                    Reader r = new InputStreamReader(url.openStream());
                    BufferedReader br = new BufferedReader(r);
                    try {
                        String className = br.readLine();
                        if (className != null) {
                            className = className.trim();
                            Class<?> clazz = cl.loadClass(className);
                            try {
                                Constructor<?> decorator = clazz.getConstructor(_DECORATOR_SIGNATURE);
                                ServletContext context = getServletContext();
                                File tempdir = (File) context.getAttribute("javax.servlet.context.tempdir");
                                ResourceLoader delegate = new DirectoryResourceLoader(tempdir);
                                loader = (ResourceLoader) decorator.newInstance(new Object[] { delegate });
                            } catch (InvocationTargetException e) {
                                loader = (ResourceLoader) clazz.newInstance();
                            } catch (NoSuchMethodException e) {
                                loader = (ResourceLoader) clazz.newInstance();
                            }
                        }
                    } finally {
                        br.close();
                    }
                } else {
                    _LOG.warning("Unable to find ResourceLoader for ResourceServlet" + " at servlet path:{0}" + "\nCause: Could not find resource:{1}", new Object[] { servletPath, key });
                    loader = new ServletContextResourceLoader(getServletContext()) {

                        @Override
                        public URL getResource(String path) throws IOException {
                            return super.getResource(path);
                        }
                    };
                }
                if (!_debug) loader = new CachingResourceLoader(loader);
            } catch (IllegalAccessException e) {
                loader = ResourceLoader.getNullResourceLoader();
            } catch (InstantiationException e) {
                loader = ResourceLoader.getNullResourceLoader();
            } catch (ClassNotFoundException e) {
                loader = ResourceLoader.getNullResourceLoader();
            } catch (IOException e) {
                loader = ResourceLoader.getNullResourceLoader();
            }
            _loaders.put(servletPath, loader);
        }
        return loader;
    }
