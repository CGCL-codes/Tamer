    private void register(String pckgname, Class<?> tosubclass) {
        String name = new String(pckgname);
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.', '/');
        URL url = ComponentsLoader.class.getResource(name);
        if (url == null) {
            logger.warn("Package " + pckgname + " doesn't exist, no components to load");
            return;
        }
        String platformName = name.replaceFirst("^.*/", "");
        logger.info("Loading components for platform " + platformName + " from " + url);
        File directory = new File(url.getFile().toString().replaceAll("%20", "\\ "));
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    String classname = files[i].substring(0, files[i].length() - 6);
                    try {
                        Class<?> c = Class.forName(pckgname + "." + classname);
                        registerTestAPIMethods(c);
                    } catch (ClassNotFoundException cnfex) {
                        logger.error("Error loading component " + classname, cnfex);
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
                        String classname = entryname.substring(0, entryname.length() - 6);
                        if (classname.startsWith("/")) {
                            classname = classname.substring(1);
                        }
                        classname = classname.replace('/', '.');
                        try {
                            Class<?> c = Class.forName(classname);
                            registerTestAPIMethods(c);
                        } catch (ClassNotFoundException cnfex) {
                            System.err.println(cnfex);
                        }
                    }
                }
            } catch (IOException ioex) {
                logger.fatal("ComponentsLoader cannot load jar file ", ioex);
            }
        }
    }
