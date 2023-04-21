    private static Set getClasses(Package pack) {
        Set classes = new LinkedHashSet();
        boolean recursive = true;
        String packageName = pack.getName();
        String packageDirName = packageName.replace('.', '/');
        Enumeration dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = (URL) dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = (JarEntry) entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            log.error("Add custom class to set error!", e);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("Scan jar file happen io exception!", e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Find class happen io exception!", e);
        }
        return classes;
    }
