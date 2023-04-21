    public static void findInJar(final List a_result, final URL a_url, Class a_tosubclass) {
        try {
            JarURLConnection conn = (JarURLConnection) a_url.openConnection();
            String starts = conn.getEntryName();
            JarFile jfile = conn.getJarFile();
            Enumeration e = jfile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                String entryname = entry.getName();
                if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length()) && entryname.endsWith(".class")) {
                    String classname = entryname.substring(0, entryname.length() - 6);
                    if (classname.startsWith("/")) {
                        classname = classname.substring(1);
                    }
                    classname = classname.replace('/', '.');
                    try {
                        Class c = Class.forName(classname);
                        if (implementsInterface(c, a_tosubclass) || extendsClass(c, a_tosubclass)) {
                            a_result.add(classname);
                        }
                    } catch (ClassNotFoundException cnfex) {
                        LOGGER.error(cnfex);
                    }
                }
            }
        } catch (IOException ioex) {
            LOGGER.error(ioex);
        }
    }
