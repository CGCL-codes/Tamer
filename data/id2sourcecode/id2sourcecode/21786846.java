    @SuppressWarnings("all")
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException, IOException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            String urlPath = JAR_PROTOCOL + directory.getPath();
            String packageEntry = packageName.replace(POINT, SLASH_URL_TYPE) + SLASH_URL_TYPE;
            URL url = new URL(urlPath);
            JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
            JarFile jar = urlConnection.getJarFile();
            Enumeration<JarEntry> enumex = jar.entries();
            while (enumex.hasMoreElements()) {
                JarEntry entry = enumex.nextElement();
                String entryName = entry.getName();
                String cleanEntryName = entryName.replace(SLASH_URL_TYPE, POINT).replace(SUFFIX_CLASS, BLANK);
                if (entryName.contains(packageEntry) && !entryName.equals(packageEntry)) {
                    classes.add(ClassLocator.class.getClassLoader().loadClass(cleanEntryName));
                }
            }
        } else {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(POINT + BLANK);
                    classes.addAll(findClasses(file, packageName + POINT + file.getName()));
                } else if (file.getName().endsWith(SUFFIX_CLASS)) {
                    classes.add(Class.forName(packageName + POINT + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }
