    private static void addElementsTo(LinkedHashSet<String> elements, String folder, ClassLoader classLoader) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(folder);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            URLConnection connection = url.openConnection();
            if (connection instanceof JarURLConnection) {
                JarURLConnection jarc = (JarURLConnection) connection;
                ZipFile zipFile = jarc.getJarFile();
                elements.add(zipFile.getName());
            } else if (connection instanceof FileURLConnection) {
                elements.add(url.getFile());
            }
        }
    }
