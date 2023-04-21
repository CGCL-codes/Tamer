    private static boolean check(String classPath, ClassLoader loader, boolean stdin, String[] configOptions, String entriesPath, PrintStream err) {
        if (classPath != null) {
            StringTokenizer st = new StringTokenizer(classPath, File.pathSeparator);
            URL[] urls = new URL[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                String elt = st.nextToken();
                try {
                    urls[i] = new File(elt).toURI().toURL();
                } catch (MalformedURLException e) {
                    print(err, "checkconfig.classpath", elt);
                    return false;
                }
            }
            loader = URLClassLoader.newInstance(urls, loader);
        }
        Properties entries = null;
        if (entriesPath != null) {
            entries = getEntries(entriesPath, err);
            if (entries == null) {
                return false;
            }
        }
        String location;
        if (configOptions.length == 0) {
            location = "(stdin)";
        } else {
            configOptions = (String[]) configOptions.clone();
            location = configOptions[0];
            configOptions[0] = "-";
        }
        Constructor configCons = getProviderConstructor(loader, err);
        if (configCons == null) {
            return false;
        }
        try {
            InputStream in;
            if (stdin) {
                in = System.in;
            } else if ("-".equals(location)) {
                in = new ByteArrayInputStream(new byte[0]);
            } else {
                try {
                    URL url = new URL(location);
                    in = url.openStream();
                } catch (MalformedURLException e) {
                    in = new FileInputStream(location);
                }
            }
            Object config;
            try {
                config = configCons.newInstance(new Object[] { new InputStreamReader(in), configOptions, loader });
            } finally {
                if (!stdin) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return check(config, entries, loader, err);
        } catch (FileNotFoundException e) {
            print(err, "checkconfig.notfound", location);
        } catch (Throwable t) {
            print(err, "checkconfig.read", location, t);
        }
        return false;
    }
