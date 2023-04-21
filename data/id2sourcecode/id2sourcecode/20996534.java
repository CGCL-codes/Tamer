    private List<String> findResource(final String location, String rootDir) throws IOException {
        final List<String> listOfClasses = new ArrayList<String>();
        final List<String> resources = new ArrayList<String>();
        String path = location;
        if (path.startsWith("/")) path = path.substring(1);
        try {
            Enumeration<?> resourceUrls = ClassPathScanner.class.getClassLoader().getResources(path);
            while (resourceUrls.hasMoreElements()) {
                URL url = (URL) resourceUrls.nextElement();
                if (url.getProtocol().equals(URL_PROTOCOL_JAR) || url.getProtocol().equals(URL_PROTOCOL_ZIP) || url.getProtocol().equals(URL_PROTOCOL_WSJAR) || url.getProtocol().equals(URL_PROTOCOL_CODE_SOURCE) && url.getPath().contains(JAR_URL_SEPARATOR)) {
                    final JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                    final JarFile jarFile = urlConnection.getJarFile();
                    final Enumeration<?> jarEnum = jarFile.entries();
                    while (jarEnum.hasMoreElements()) {
                        final JarEntry entry = (JarEntry) jarEnum.nextElement();
                        String entryName = entry.getName();
                        if (entryName.endsWith(".class") && entryName.startsWith(location)) {
                            entryName = entryName.replaceAll("/", ".").substring(0, entryName.length() - 6);
                            resources.add(entryName);
                        }
                    }
                } else {
                    final File file = new File(url.getFile());
                    for (String clazz : exploreFolders(file, new File(url.getFile()).getAbsolutePath().length(), path, listOfClasses)) {
                        resources.add(clazz);
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Unable to parse the configuration file as the file does not exist");
        }
        return resources;
    }
