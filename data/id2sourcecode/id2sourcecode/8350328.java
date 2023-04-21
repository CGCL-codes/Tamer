    @SuppressWarnings("unchecked")
    public static <T> List<Class<T>> findSubclasses(String packageFQN, Class<T> superclass) throws IOException {
        String packageResourceName = packageFQN.replace(".", "/");
        List<String> binaryClassNames = new LinkedList<String>();
        ArrayList<URL> resources = Collections.list(Thread.currentThread().getContextClassLoader().getResources(packageResourceName));
        for (URL url : resources) {
            URLConnection connection = url.openConnection();
            if (connection instanceof JarURLConnection) {
                JarURLConnection jarCon = (JarURLConnection) connection;
                String prefix = jarCon.getEntryName();
                for (JarEntry entry : Collections.list(jarCon.getJarFile().entries())) {
                    String name = entry.getName();
                    if (name.startsWith(prefix) && name.toLowerCase().endsWith(ReflectionUtils.CLASS_FILE_EXTENSION)) {
                        binaryClassNames.add(name);
                    }
                }
            } else {
                try {
                    File[] fittingFiles = new File(url.toURI()).listFiles(new FileFilter() {

                        public boolean accept(File file) {
                            return file.isFile() && file.getName().toLowerCase().endsWith(ReflectionUtils.CLASS_FILE_EXTENSION);
                        }
                    });
                    for (File file : fittingFiles) {
                        binaryClassNames.add(file.getPath());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        List<Class<T>> sublasses = new LinkedList<Class<T>>();
        for (String name : binaryClassNames) {
            String filePath = name.replace(File.separatorChar, '/');
            int indexOf = filePath.indexOf(packageResourceName);
            if (indexOf > -1) {
                try {
                    Class<?> forName = Class.forName(filePath.substring(indexOf, filePath.length() - 6).replace('/', '.'));
                    if (superclass.isAssignableFrom(forName)) {
                        sublasses.add((Class<T>) forName);
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }
        return sublasses;
    }
