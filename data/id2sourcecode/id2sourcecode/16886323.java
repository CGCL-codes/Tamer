    public static void launch(final String mainClassName, boolean gui) {
        File lockFile = new File(System.getProperty("user.home") + File.separator + "." + mainClassName.hashCode() + ".lock");
        try {
            FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
            FileLock lock = channel.tryLock();
            if (lock == null) {
                return;
            } else {
                lockFile.deleteOnExit();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        File[] libs = new File(APPLICATION_PATH + "lib").listFiles(new JarFileNameFilter(BLACKLIST));
        int libsLength = libs != null ? libs.length : 0;
        URL[] libUrls = new URL[libsLength];
        for (int n = 0, i = libUrls.length; n < i; n++) {
            try {
                libUrls[n] = libs[n].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        urlLoader = new URLClassLoader(libUrls, ClassLoader.getSystemClassLoader());
        if (gui && !System.getProperty("java.fullversion", "").contains("GNU")) {
            try {
                urlLoader.loadClass("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
                UIManager.installLookAndFeel("JGoodies Plastic XP", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
                Hashtable<Object, Object> uiDefaults = UIManager.getDefaults();
                uiDefaults.put("ClassLoader", urlLoader);
                Enumeration<?> enumeration = uiDefaults.keys();
                while (enumeration.hasMoreElements()) {
                    String className = enumeration.nextElement().toString();
                    if (className.endsWith("UI")) {
                        Class<?> uiClass = urlLoader.loadClass(uiDefaults.get(className).toString());
                        uiDefaults.put(uiClass.getName(), uiClass);
                    }
                }
            } catch (Exception exception) {
            }
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    urlLoader.loadClass(mainClassName).newInstance();
                } catch (Exception exception) {
                    Logger.log(exception);
                }
            }
        });
    }
