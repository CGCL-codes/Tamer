    protected void init() {
        initialized = true;
        ClassLoader parentLoader = PluginManager.class.getClassLoader();
        LinkedList<URL> classPathJars = new LinkedList<URL>();
        MultiMap<URL, URL> embeddedJars = new MultiHashMap<URL, URL>();
        LinkedList<URL> pluginJars = new LinkedList<URL>();
        Map<URL, JarFile> classSearchJarFiles = new HashMap<URL, JarFile>();
        MultiMap<URL, String> classLoadJarFiles = new MultiHashMap<URL, String>();
        for (File f : getPluginDirs()) {
            if (!f.exists() || !f.isDirectory()) continue;
            File sharedDir = new File(f, "shared");
            if (sharedDir.exists() && sharedDir.isDirectory()) {
                LinkedList<URL> sharedLibs = new LinkedList<URL>();
                for (File d : sharedDir.listFiles(jarFilter)) {
                    try {
                        new JarFile(d);
                        sharedLibs.add(d.toURL());
                    } catch (IOException e) {
                        System.err.println("Could not read shared jar " + d);
                    }
                }
                if (sharedLibs.size() > 0) {
                    parentLoader = new URLClassLoader(sharedLibs.toArray(new URL[sharedLibs.size()]));
                }
            }
            File[] temp = f.listFiles(jarFilter);
            for (File jar : temp) {
                try {
                    JarFile jf = new JarFile(jar);
                    classPathJars.add(jar.toURL());
                    pluginJars.add(jar.toURL());
                    URL[] urls = { jar.toURL() };
                    URLClassLoader pluginLoader = new URLClassLoader(urls);
                    boolean doLibrarySearch = true;
                    boolean doClassSearch = true;
                    URL classIndexFile = pluginLoader.getResource("install.xml");
                    if (classIndexFile != null) {
                        XMLDecoder decoder = new XMLDecoder(classIndexFile.openStream());
                        InstallerConfig config = (InstallerConfig) decoder.readObject();
                        if (config != null) {
                            String file = null;
                            try {
                                if (config.getLibs() != null) {
                                    for (String s : config.getLibs()) {
                                        file = s;
                                        URL embeddedURL = pluginLoader.getResource(s);
                                        if (embeddedURL != null) embeddedJars.add(jar.toURL(), embeddedURL);
                                    }
                                    doLibrarySearch = false;
                                }
                            } catch (Throwable t) {
                                Logger logger = Logger.getLogger("plugins");
                                logger.log(Level.WARNING, "Unable to load embedded library " + file, t);
                            }
                            String className = null;
                            if (config.getClasses() != null) {
                                for (String s : config.getClasses()) {
                                    className = s;
                                    classLoadJarFiles.add(jar.toURL(), s);
                                }
                                doClassSearch = false;
                            }
                        }
                    }
                    if (doLibrarySearch) {
                        JarEntry libEntry = jf.getJarEntry("lib/");
                        if (libEntry != null) {
                            if (libEntry.isDirectory()) {
                                java.util.Enumeration<JarEntry> e = jf.entries();
                                while (e.hasMoreElements()) {
                                    JarEntry je = e.nextElement();
                                    if (je.getName().startsWith("lib/") && je.getName().endsWith(".jar")) {
                                        URL embeddedURL = pluginLoader.getResource(je.getName());
                                        embeddedJars.add(jar.toURL(), embeddedURL);
                                    }
                                }
                            }
                        }
                    }
                    if (doClassSearch) classSearchJarFiles.put(jar.toURL(), jf);
                } catch (IOException e) {
                    Logger logger = Logger.getLogger("plugins");
                    logger.log(Level.WARNING, "Could not read plugin jar " + jar, e);
                }
            }
        }
        this.pluginJars = pluginJars;
        for (URL url : classPathJars) {
            LinkedList<URL> urlList = new LinkedList<URL>();
            for (URL embedded : embeddedJars.get(url)) {
                try {
                    File temp = File.createTempFile("templib", ".jar");
                    temp.deleteOnExit();
                    IOUtil.dumpAndClose(embedded.openStream(), new FileOutputStream(temp));
                    urlList.add(temp.toURL());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            urlList.add(url);
            URLClassLoader loader = new URLClassLoader(urlList.toArray(new URL[urlList.size()]), parentLoader);
            classLoaderMap.put(url, loader);
        }
        URL[] urls = classPathJars.toArray(new URL[classPathJars.size()]);
        for (URL url : classSearchJarFiles.keySet()) {
            ClassLoader pluginLoader = getClassLoader(url);
            JarFile jf = classSearchJarFiles.get(url);
            java.util.Enumeration<JarEntry> e = jf.entries();
            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (!je.getName().startsWith("lib/") && je.getName().endsWith(".class") && je.getName().length() > 6) {
                    String name = je.getName().replace('/', '.').substring(0, je.getName().length() - 6);
                    try {
                        Class<?> c = pluginLoader.loadClass(name);
                        classMap.add(url, c);
                    } catch (ClassNotFoundException e1) {
                        Logger logger = Logger.getLogger("plugins");
                        logger.log(Level.WARNING, "Could not read class file " + name + " during automatic search of jar " + url, e1);
                    }
                }
            }
        }
        for (URL url : classLoadJarFiles.keySet()) {
            ClassLoader pluginLoader = getClassLoader(url);
            for (String s : classLoadJarFiles.get(url)) {
                try {
                    Class<?> c = pluginLoader.loadClass(s);
                    classMap.add(url, c);
                } catch (ClassNotFoundException e1) {
                    Logger logger = Logger.getLogger("plugins");
                    logger.log(Level.WARNING, "Could not read class file " + s + " specified in installation preferences of jar " + url, e1);
                }
            }
        }
    }
