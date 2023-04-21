    private static final Iterator getResources(String directory) {
        ClassLoader context = Thread.currentThread().getContextClassLoader();
        List resources = new ArrayList();
        ClassLoader cl = JPCApplication.class.getClassLoader();
        if (!(cl instanceof URLClassLoader)) throw new IllegalStateException();
        URL[] urls = ((URLClassLoader) cl).getURLs();
        int slash = directory.lastIndexOf("/");
        String dir = directory.substring(0, slash + 1);
        for (int i = 0; i < urls.length; i++) {
            if (!urls[i].toString().endsWith(".jar")) continue;
            try {
                JarInputStream jarStream = new JarInputStream(urls[i].openStream());
                while (true) {
                    ZipEntry entry = jarStream.getNextEntry();
                    if (entry == null) break;
                    if (entry.isDirectory()) continue;
                    String name = entry.getName();
                    slash = name.lastIndexOf("/");
                    String thisDir = "";
                    if (slash >= 0) thisDir = name.substring(0, slash + 1);
                    if (!dir.equals(thisDir)) continue;
                    resources.add(name);
                }
                jarStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream stream = context.getResourceAsStream(directory);
        try {
            if (stream != null) {
                Reader r = new InputStreamReader(stream);
                StringBuilder sb = new StringBuilder();
                char[] buffer = new char[1024];
                try {
                    while (true) {
                        int length = r.read(buffer);
                        if (length < 0) {
                            break;
                        }
                        sb.append(buffer, 0, length);
                    }
                } finally {
                    r.close();
                }
                String as[] = sb.toString().split("\n");
                for (int i = 0; i < as.length; i++) {
                    String s = as[i];
                    if (context.getResource(directory + s) != null) {
                        resources.add(s);
                    }
                }
            }
        } catch (IOException e) {
            LOGGING.log(Level.INFO, "Exception reading images directory stream", e);
        }
        return resources.iterator();
    }
