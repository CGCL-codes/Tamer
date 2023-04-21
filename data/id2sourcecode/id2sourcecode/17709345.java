    private Vector<Class<? extends Iterator>> loadDistIterators() {
        TreeMap<String, Class<? extends Iterator>> iteratorTM = new TreeMap<String, Class<? extends Iterator>>();
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            URL url = cl.getResource(ITERATOR_PACKAGE.replace(".", "/"));
            File dir = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    String classLoc = ITERATOR_PACKAGE + "." + f.getName().substring(0, f.getName().indexOf("."));
                    try {
                        Class<?> c = cl.loadClass(classLoc);
                        if ((c.getModifiers() & Modifier.ABSTRACT) == 0 && Iterator.class.isAssignableFrom(c)) iteratorTM.put(c.getSimpleName(), c.asSubclass(Iterator.class));
                    } catch (Exception ex) {
                        System.out.println("could not load " + classLoc);
                        ex.printStackTrace();
                    }
                }
            } else {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarFile = conn.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String className = entry.getName();
                    if (className.startsWith(ITERATOR_PACKAGE.replace(".", "/")) && className.endsWith(".class")) {
                        String classLoc = className.substring(0, className.indexOf(".class")).replace("/", ".");
                        Class<?> c = cl.loadClass(classLoc);
                        if ((c.getModifiers() & Modifier.ABSTRACT) == 0 && Iterator.class.isAssignableFrom(c)) iteratorTM.put(c.getSimpleName(), c.asSubclass(Iterator.class));
                    }
                }
            }
            return new Vector<Class<? extends Iterator>>(iteratorTM.values());
        } catch (SecurityException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
