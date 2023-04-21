    void loadAllLaunchables() {
        if (allLoaded) {
            return;
        }
        JApplet applet = org.opensourcephysics.display.OSPRuntime.applet;
        for (int i = 0; i < jarNames.length; i++) {
            JarFile jar = null;
            try {
                if (applet == null) {
                    jar = new JarFile(jarNames[i]);
                } else {
                    String path = XML.getResolvedPath(jarNames[i], applet.getCodeBase().toExternalForm());
                    URL url = new URL("jar:" + path + "!/");
                    JarURLConnection conn = (JarURLConnection) url.openConnection();
                    jar = conn.getJarFile();
                }
            } catch (IOException ex) {
                OSPLog.info(ex.getClass().getName() + ": " + ex.getMessage());
            } catch (SecurityException ex) {
                OSPLog.info(ex.getClass().getName() + ": " + ex.getMessage());
            }
            if (jar == null) {
                continue;
            }
            for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
                JarEntry entry = (JarEntry) e.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && name.indexOf("$") == -1) {
                    name = name.substring(0, name.indexOf(".class"));
                    int j = name.indexOf("/");
                    while (j != -1) {
                        name = name.substring(0, j) + "." + name.substring(j + 1);
                        j = name.indexOf("/");
                    }
                    if (get(name) != null) {
                        continue;
                    }
                    try {
                        Class next = smartLoadClass(name);
                        if (Launcher.isLaunchable(next)) {
                            put(name, next);
                        }
                    } catch (ClassNotFoundException ex) {
                    } catch (NoClassDefFoundError err) {
                        OSPLog.info(err.toString());
                    }
                }
            }
        }
        allLoaded = true;
    }
