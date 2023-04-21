    private void getAvailableClassNames() {
        listedClassNames = true;
        try {
            HashSet buffer = new HashSet();
            URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
            URL[] urls = cl.getURLs();
            for (int i = 0; i < urls.length; i++) {
                if (!urls[i].toString().endsWith(".jar")) continue;
                try {
                    URLConnection conn = urls[i].openConnection();
                    conn.setUseCaches(true);
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    JarInputStream jin = new JarInputStream(conn.getInputStream());
                    while (true) {
                        JarEntry jen = jin.getNextJarEntry();
                        if (jen == null) break;
                        String name = jen.getName().trim();
                        if (name.endsWith(".class")) name = name.substring(0, name.length() - 6);
                        name = name.replace("/", ".");
                        name = name.replace("\\", ".");
                        buffer.add(name);
                    }
                    jin.close();
                } catch (Exception e) {
                    System.out.println("Warning: exception listing contents of JAR resource " + urls[i]);
                    e.printStackTrace();
                }
            }
            availableClassNames = buffer;
        } catch (Exception e) {
        }
    }
