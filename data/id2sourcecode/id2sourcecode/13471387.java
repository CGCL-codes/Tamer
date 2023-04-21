    private List<JarFile> webArchives(ExternalContext externalContext) throws IOException {
        List<JarFile> list = new ArrayList<JarFile>();
        Set<String> paths = externalContext.getResourcePaths(WEB_LIB_PREFIX);
        if (paths == null || paths.isEmpty()) {
            Iterator<URL> it = ClassUtils.getResources(FACES_CONFIG_IMPLICIT, this);
            while (it.hasNext()) {
                URL url = it.next();
                JarFile jarFile = getJarFile(url);
                if (jarFile != null) {
                    list.add(jarFile);
                }
            }
            URL[] urls = Classpath.search(getClassLoader(), META_INF_PREFIX, FACES_CONFIG_SUFFIX);
            for (int i = 0; i < urls.length; i++) {
                JarFile jarFile = getJarFile(urls[i]);
                if (jarFile != null) {
                    list.add(jarFile);
                }
            }
        } else {
            for (Object pathObject : paths) {
                String path = (String) pathObject;
                if (!path.endsWith(".jar")) {
                    continue;
                }
                URL url = externalContext.getResource(path);
                String jarURLString = "jar:" + url.toString() + "!/";
                url = new URL(jarURLString);
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                JarEntry signal = jarFile.getJarEntry(FACES_CONFIG_IMPLICIT);
                if (signal == null) {
                    for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
                        JarEntry entry = e.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(META_INF_PREFIX) && name.endsWith(FACES_CONFIG_SUFFIX)) {
                            signal = entry;
                            break;
                        }
                    }
                }
                if (signal == null) {
                    if (log.isLoggable(Level.FINEST)) {
                        log.finest("Skip JAR file " + path + " because it has no META-INF/faces-config.xml resource");
                    }
                    continue;
                }
                list.add(jarFile);
            }
        }
        return list;
    }
