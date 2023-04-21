    public static synchronized NSBundle bundleWithURL(URL url) {
        NSBundle result = null;
        String sep = System.getProperty("file.separator");
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            File f = new File(url.getPath());
            if (!f.exists()) {
                NSLog.err.appendln("Bundle not found: " + url);
                return null;
            }
            StringBuffer filename = new StringBuffer(f.getName());
            int extensionIndex = filename.lastIndexOf(".");
            if (extensionIndex == -1) {
                NSLog.err.appendln("Named URL does not point to a bundle with an extension: " + url);
                return null;
            }
            String basename = filename.substring(0, extensionIndex);
            String extension = filename.substring(extensionIndex + 1, filename.length());
            System.out.println("basename: " + basename);
            System.out.println("extension: " + extension);
            result = new NSBundle();
            result.name = basename;
            result.isFramework = extension.equals("framework");
            if (f.isDirectory()) {
                try {
                    File javadir = new File(f.getCanonicalPath() + sep + "Contents" + sep + "Resources" + sep + "Java");
                    System.out.println(javadir);
                    System.out.println(javadir.exists());
                    File[] jars = javadir.listFiles();
                } catch (IOException e) {
                }
            } else {
                throw new RuntimeException("Compressed bundle files not currently supported.");
            }
            throw new RuntimeException("Method not finished.");
        } else {
            try {
                JarInputStream j = new JarInputStream(url.openStream());
                JarEntry entry1 = j.getNextJarEntry();
                JarFile f;
                throw new RuntimeException("Method not finished.");
            } catch (IOException e) {
                NSLog.err.appendln("IOException loading framework jar from URL " + url + " - message: " + e.getLocalizedMessage());
                StringWriter stacktrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stacktrace));
                NSLog.err.appendln(stacktrace);
                return null;
            }
        }
    }
