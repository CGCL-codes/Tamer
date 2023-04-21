    private static Class<?>[] getClassesByWar(String packageName) {
        String resourceBase = WebAppContext.getCurrentWebAppContext() != null ? WebAppContext.getCurrentWebAppContext().getResourceBase() : "";
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        packageName = packageName.replaceAll("\\.", "/");
        try {
            URL url = new URL(resourceBase);
            JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
            JarFile jarFile = jarConnection.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                if ((jarEntry.getName().contains(packageName)) && (jarEntry.getName().endsWith(".class"))) {
                    classes.add(Class.forName(jarEntry.getName().replaceAll("/", "\\.").substring(jarEntry.getName().indexOf(packageName), jarEntry.getName().length() - 6)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Class<?>[] classesA = new Class<?>[classes.size()];
        classes.toArray(classesA);
        return classesA;
    }
