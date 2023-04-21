    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.err.println("No main class specified.");
                System.err.println("usage: CLILoader <fedora main class> <options>");
                System.exit(1);
            }
            String webInfLib = System.getProperty("fedora.web.inf.lib");
            if (webInfLib == null) {
                System.err.println("fedora.web.inf.lib not defined");
                System.exit(1);
            }
            File webInfLibDir = new File(webInfLib);
            if (!webInfLibDir.exists()) {
                System.err.println("path specified by fedora.web.inf.lib doesn't exist");
                System.exit(1);
            }
            if (!webInfLibDir.isDirectory()) {
                System.err.println("path specified by fedora.web.inf.lib not a directory");
                System.exit(1);
            }
            String[] paths = webInfLibDir.list();
            URL[] urls = new URL[paths.length];
            for (int i = 0; i < paths.length; i++) {
                urls[i] = new File(webInfLibDir, paths[i]).toURI().toURL();
            }
            CLILoader loader = new CLILoader(urls);
            Thread.currentThread().setContextClassLoader(loader);
            Class<?> rebuild = loader.findClass(args[0]);
            Method main = rebuild.getMethod("main", new Class<?>[] { String[].class });
            String[] modArgs = new String[args.length - 1];
            if (args.length > 1) {
                System.arraycopy(args, 1, modArgs, 0, modArgs.length);
            }
            main.invoke(rebuild, new Object[] { modArgs });
        } catch (Exception e) {
            System.err.println("Error executing main class: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }
