    public SmedFW(PluginInformation info) {
        super(info);
        JarEntry e = null;
        BufferedInputStream inp = null;
        byte[] buffer = new byte[16384];
        int len;
        List<String> lib = new ArrayList<String>();
        lib.add("org.apache.felix.scr-1.4.0.jar");
        lib.add("osgi.cmpn-4.2.1.jar");
        String eName = null;
        String pluginDirName = Main.pref.getPluginsDirectory().getAbsolutePath() + "/";
        File fwDir = new File(pluginDirName + FW_BIN_DIR);
        if (!fwDir.exists()) fwDir.mkdir();
        SmedFWFile fwplugDir = new SmedFWFile(pluginDirName + FW_BUNDLE_LOCATION);
        if (!fwplugDir.exists()) fwplugDir.mkdir();
        File[] jars = fwplugDir.listFiles(new JARFileFilter());
        try {
            JarFile file = new JarFile(pluginDirName + FW_SMED_JAR);
            boolean fwFound = false;
            FileOutputStream pfos = null;
            Enumeration<JarEntry> ent = file.entries();
            while (ent.hasMoreElements()) {
                e = ent.nextElement();
                eName = e.getName();
                if (eName.endsWith(".jar")) {
                    if (eName.equals(FW_NAME)) {
                        pfos = new FileOutputStream(pluginDirName + FW_BIN_DIR + "/" + FW_NAME);
                        fwFound = true;
                    } else {
                        pfos = new FileOutputStream(pluginDirName + FW_BUNDLE_LOCATION + "/" + eName);
                        fwFound = false;
                    }
                    if (fwFound || lib.contains(eName) || fwplugDir.needUpdate(jars, eName)) {
                        BufferedOutputStream pos = new BufferedOutputStream(pfos);
                        inp = new BufferedInputStream(file.getInputStream(e));
                        while ((len = inp.read(buffer)) > 0) pos.write(buffer, 0, len);
                        pos.flush();
                        pos.close();
                        inp.close();
                        pfos.close();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        RunFW runFW = new RunFW();
        runFW.init();
        System.out.println("SmedFW (OSGi-Implementation) noch nicht weiter programmiert");
    }
