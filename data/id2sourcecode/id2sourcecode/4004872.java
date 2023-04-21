    public void readPlugins(String base, JFrame parent) throws Exception {
        String baseURL = base;
        ProgressMonitor monitor;
        if (base == null) {
            baseURL = TiledConfiguration.root().get("pluginsDir", "plugins");
        }
        File dir = new File(baseURL);
        if (!dir.exists() || !dir.canRead()) {
            return;
        }
        int total = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            String aPath = file.getAbsolutePath();
            if (aPath.endsWith(".jar")) {
                total++;
            }
        }
        monitor = new ProgressMonitor(parent, "Loading plugins", "", 0, total - 1);
        monitor.setProgress(0);
        monitor.setMillisToPopup(0);
        monitor.setMillisToDecideToPopup(0);
        for (int i = 0; i < files.length; i++) {
            String aPath = files[i].getAbsolutePath();
            String aName = aPath.substring(aPath.lastIndexOf(File.separatorChar) + 1);
            if (!aPath.endsWith(".jar")) {
                continue;
            }
            try {
                monitor.setNote("Reading " + aName + "...");
                JarFile jf = new JarFile(files[i]);
                monitor.setProgress(i);
                if (jf.getManifest() == null) continue;
                String readerClassName = jf.getManifest().getMainAttributes().getValue("Reader-Class");
                String writerClassName = jf.getManifest().getMainAttributes().getValue("Writer-Class");
                Class readerClass = null;
                Class writerClass = null;
                if (readerClassName == null && writerClassName == null) {
                    continue;
                }
                monitor.setNote("Loading " + aName + "...");
                addURL(new File(aPath).toURI().toURL());
                if (readerClassName != null) {
                    JarEntry reader = jf.getJarEntry(readerClassName.replace('.', '/') + ".class");
                    if (reader != null) {
                        readerClass = loadFromJar(jf, reader, readerClassName);
                    } else System.err.println("Manifest entry " + readerClassName + " does not match any class in the jar.");
                }
                if (writerClassName != null) {
                    JarEntry writer = jf.getJarEntry(writerClassName.replace('.', '/') + ".class");
                    if (writer != null) {
                        writerClass = loadFromJar(jf, writer, writerClassName);
                    } else System.err.println("Manifest entry " + writerClassName + " does not match any class in the jar.");
                }
                boolean bPlugin = false;
                if (isReader(readerClass)) {
                    bPlugin = true;
                }
                if (isWriter(writerClass)) {
                    bPlugin = true;
                }
                if (bPlugin) {
                    if (readerClass != null) _add(readerClass);
                    if (writerClass != null) _add(writerClass);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
