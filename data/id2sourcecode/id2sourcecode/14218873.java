    public XXXJarClassLoader(ClassLoader parent) {
        super(parent);
        initLogger();
        hmClass = new HashMap<String, Class<?>>();
        lstJarFile = new ArrayList<JarFileInfo>();
        hsDeleteOnExit = new HashSet<File>();
        String sUrlTopJAR = null;
        try {
            pd = getClass().getProtectionDomain();
            CodeSource cs = pd.getCodeSource();
            URL urlTopJAR = cs.getLocation();
            if (!urlTopJAR.getProtocol().equals("jar")) {
                urlTopJAR = new URL("jar:" + urlTopJAR.toString() + "!/");
            }
            sUrlTopJAR = URLDecoder.decode(urlTopJAR.getFile(), "UTF-8");
            logInfo(LogArea.JAR, "Loading from top JAR: %s", sUrlTopJAR);
            JarURLConnection jarCon = (JarURLConnection) urlTopJAR.openConnection();
            JarFile jarFile = jarCon.getJarFile();
            JarFileInfo jarInfo = new JarFileInfo(jarFile.getName(), jarFile, null);
            loadJar(jarInfo);
        } catch (IOException e) {
            logInfo(LogArea.JAR, "Not a JAR: %s %s", sUrlTopJAR, e.toString());
            return;
        }
        checkShading();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                shutdown();
            }
        });
    }
