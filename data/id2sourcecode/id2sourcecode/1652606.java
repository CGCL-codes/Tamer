    public static void main(String[] args) throws IOException {
        String base = System.getenv(GLOBUS_LOCATION);
        ArrayList<URL> libs = new ArrayList<URL>();
        URL[] urlJars;
        String[] launchArgs;
        String launchClass = "org.globus.wsrf.container.ServiceContainer";
        URLClassLoader loader;
        File baseDir = new File(base);
        if (!baseDir.exists() || !baseDir.isDirectory() || !baseDir.canRead()) {
            throw new IOException("Can not open Globus Directory");
        }
        try {
            libs.add(baseDir.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File libDir = new File(baseDir, "lib");
        if (!libDir.exists() || !libDir.isDirectory() || !libDir.canRead()) {
            throw new IOException("Can not open Lib Directory");
        }
        try {
            File[] jars = libDir.listFiles(new JarFilter(null));
            for (int i = 0; i < jars.length; i++) {
                libs.add(jars[i].toURL());
            }
        } catch (IOException e) {
            throw new IOException("Can not add Lib Directory");
        }
        launchArgs = new String[args.length - 1];
        System.arraycopy(args, 0, launchArgs, 0, launchArgs.length);
        urlJars = new URL[libs.size()];
        urlJars = (URL[]) libs.toArray(urlJars);
        loader = new URLClassLoader(urlJars);
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class mainClass = loader.loadClass(launchClass);
            Method mainMethod = mainClass.getMethod("main", MAIN_PARAMS_TYPE);
            mainMethod.invoke(null, new Object[] { launchArgs });
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
