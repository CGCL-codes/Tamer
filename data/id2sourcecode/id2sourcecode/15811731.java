    public static Hashtable loadDirectory(String packDir) {
        Hashtable classTable = new Hashtable(5);
        URL url = PluginLoader.class.getResource("/edu/ucla/loni/LOVE/");
        try {
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile();
                Enumeration entries = jarFile.entries();
                String path = packDir.replace('.', '/');
                while (entries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        if (entryName.endsWith(".class")) {
                            int index = entryName.lastIndexOf('/');
                            if (entryName.substring(0, index).equals(path)) {
                                String className = entryName.substring(index + 1, entryName.length() - 6);
                                Class aClass = loadAClass(packDir + "." + className);
                                if (aClass != null) {
                                    classTable.put(className, aClass);
                                }
                            }
                        }
                    }
                }
                return classTable;
            } else {
                url = PluginLoader.class.getResource("/" + packDir.replace('.', '/'));
                File classDir = new File(url.toURI());
                String[] fileList = classDir.list();
                if (fileList != null) {
                    int i;
                    for (i = 0; i < fileList.length; i++) {
                        if (fileList[i].endsWith(".class")) {
                            String className = fileList[i].substring(0, fileList[i].indexOf("."));
                            Class aClass = loadAClass(packDir + "." + className);
                            if (aClass != null) {
                                classTable.put(className, aClass);
                            }
                        }
                    }
                    return classTable;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
