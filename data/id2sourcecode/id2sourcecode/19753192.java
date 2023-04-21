    public static Set<Class> findMigrationClassesInPackage(String packageName, ClassWalkerAcceptor classWalkerAcceptor) {
        Set<Class> acceptedClasses = new HashSet<Class>();
        try {
            String packageOnly = packageName;
            boolean recursive = false;
            if (packageName.endsWith(".*")) {
                packageOnly = packageName.substring(0, packageName.lastIndexOf(".*"));
                recursive = true;
            }
            String packageDirName = packageOnly.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                if ("file".equals(url.getProtocol())) {
                    findClassesInDirPackage(packageOnly, URLDecoder.decode(url.getFile(), "UTF-8"), recursive, acceptedClasses, classWalkerAcceptor);
                } else if ("jar".equals(url.getProtocol())) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (!name.endsWith("/")) {
                            String className = name.replaceAll("/", ".").replaceAll("\\.class", "");
                            checkValidClass(className, acceptedClasses, classWalkerAcceptor);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return acceptedClasses;
    }
