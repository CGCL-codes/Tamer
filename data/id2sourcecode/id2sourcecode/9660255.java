    public static Collection<Class<?>> findSubClasses(String pkgName, Class<?> baseClass) {
        AssertUtils.assertNonNullArg(pkgName);
        AssertUtils.assertNonNullArg(baseClass);
        String pkgPath = new String(pkgName);
        if (!pkgPath.startsWith("/")) {
            pkgPath = "/" + pkgPath;
        }
        pkgPath = pkgPath.replace('.', '/');
        URL url = JavaUtils.class.getResource(pkgPath);
        if (url == null) {
            throw new IllegalStateException("Can't find package path." + " It may be in a jar with missing ancestor packages. path=" + pkgPath);
        }
        File directory = new File(url.getFile());
        Collection<Class<?>> subClasses = new ArrayList<Class<?>>();
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    String className = files[i].substring(0, files[i].length() - 6);
                    String subClassName = pkgName + "." + className;
                    try {
                        Class<?> subClass = Class.forName(subClassName);
                        if (baseClass.isInstance(subClass)) {
                            subClasses.add(subClass);
                        }
                    } catch (ClassNotFoundException cnfex) {
                        throw new IllegalStateException("Can't find sub class file. class=" + subClassName);
                    }
                }
            }
        } else {
            try {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                String starts = conn.getEntryName();
                JarFile jfile = conn.getJarFile();
                Enumeration<JarEntry> e = jfile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    String entryname = entry.getName();
                    if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length()) && entryname.endsWith(".class")) {
                        String subClassName = entryname.substring(0, entryname.length() - 6);
                        if (subClassName.startsWith("/")) subClassName = subClassName.substring(1);
                        subClassName = subClassName.replace('/', '.');
                        try {
                            Class<?> subClass = Class.forName(subClassName);
                            if (baseClass.isInstance(subClass)) {
                                subClasses.add(subClass);
                            }
                        } catch (ClassNotFoundException cnfex) {
                            throw new IllegalStateException("Can't find sub class jar entry. class=" + subClassName);
                        }
                    }
                }
            } catch (IOException ioex) {
                throw new IllegalStateException("Can't find jar file. url=" + url);
            }
        }
        return subClasses;
    }
