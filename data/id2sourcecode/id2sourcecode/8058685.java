    public static void main(String[] arguments) throws Exception {
        String className = null;
        Class clazz = Loader.class;
        ClassLoader loader = clazz.getClassLoader();
        Vector res = new Vector();
        try {
            Enumeration en = loader.getResources("META-INF/MANIFEST.MF");
            while (en.hasMoreElements()) {
                res.add((URL) en.nextElement());
            }
            for (int i = res.size() - 1; i >= 0; i--) {
                URL jarurl = (URL) res.elementAt(i);
                try {
                    JarURLConnection jarConnection = (JarURLConnection) jarurl.openConnection();
                    Manifest mf = jarConnection.getManifest();
                    Attributes attrs = (Attributes) mf.getAttributes("com/sun/star/lib/loader/Loader.class");
                    if (attrs != null) {
                        className = attrs.getValue("Application-Class");
                        if (className != null) break;
                    }
                } catch (IOException e) {
                    System.err.println("com.sun.star.lib.loader.Loader::" + "main: bad manifest file: " + e);
                }
            }
        } catch (IOException e) {
            System.err.println("com.sun.star.lib.loader.Loader::" + "main: cannot get manifest resources: " + e);
        }
        String[] args = arguments;
        if (className == null) {
            className = System.getProperty("Application-Class");
            if (className == null) {
                if (arguments.length > 0) {
                    className = arguments[0];
                    args = new String[arguments.length - 1];
                    System.arraycopy(arguments, 1, args, 0, args.length);
                } else {
                    throw new IllegalArgumentException("The name of the class to be loaded must be either " + "specified in the Main-Class attribute of the " + "com/sun/star/lib/loader/Loader.class entry " + "of the manifest file or as a command line argument.");
                }
            }
        }
        if (className != null) {
            ClassLoader cl = getCustomLoader();
            Class c = cl.loadClass(className);
            Method m = c.getMethod("main", new Class[] { String[].class });
            m.invoke(null, new Object[] { args });
        }
    }
