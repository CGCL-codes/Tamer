    public void init() {
        if (midlet != null) {
            return;
        }
        MIDletSystemProperties.applyToJavaSystemProperties = false;
        MIDletBridge.setMicroEmulator(this);
        URL baseURL = getCodeBase();
        if (baseURL != null) {
            accessibleHost = baseURL.getHost();
        }
        recordStoreManager = new MemoryRecordStoreManager();
        setLayout(new BorderLayout());
        add(devicePanel, "Center");
        DeviceImpl device;
        String deviceParameter = getParameter("device");
        if (deviceParameter == null) {
            device = new J2SEDevice();
            DeviceFactory.setDevice(device);
            device.init(emulatorContext);
        } else {
            try {
                Class cl = Class.forName(deviceParameter);
                device = (DeviceImpl) cl.newInstance();
                DeviceFactory.setDevice(device);
                device.init(emulatorContext);
            } catch (ClassNotFoundException ex) {
                try {
                    device = DeviceImpl.create(emulatorContext, Main.class.getClassLoader(), deviceParameter, J2SEDevice.class);
                    DeviceFactory.setDevice(device);
                } catch (IOException ex1) {
                    Logger.error(ex);
                    return;
                }
            } catch (IllegalAccessException ex) {
                Logger.error(ex);
                return;
            } catch (InstantiationException ex) {
                Logger.error(ex);
                return;
            }
        }
        devicePanel.init();
        manifest.clear();
        try {
            URL url = getClass().getClassLoader().getResource("META-INF/MANIFEST.MF");
            manifest.load(url.openStream());
            if (manifest.getProperty("MIDlet-Name") == null) {
                manifest.clear();
            }
        } catch (IOException e) {
            Logger.error(e);
        }
        String midletClassName = null;
        String jadFile = getParameter("jad");
        if (jadFile != null) {
            InputStream jadInputStream = null;
            try {
                URL jad = new URL(getCodeBase(), jadFile);
                jadInputStream = jad.openStream();
                manifest.load(jadInputStream);
                Vector entries = manifest.getMidletEntries();
                if (entries.size() > 0) {
                    JadMidletEntry entry = (JadMidletEntry) entries.elementAt(0);
                    midletClassName = entry.getClassName();
                }
            } catch (IOException e) {
            } finally {
                if (jadInputStream != null) {
                    try {
                        jadInputStream.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        if (midletClassName == null) {
            midletClassName = getParameter("midlet");
            if (midletClassName == null) {
                Logger.debug("There is no midlet parameter");
                return;
            }
        }
        MIDletResourceLoader.classLoader = this.getClass().getClassLoader();
        Class midletClass;
        try {
            midletClass = Class.forName(midletClassName);
        } catch (ClassNotFoundException ex) {
            Logger.error("Cannot find " + midletClassName + " MIDlet class");
            return;
        }
        try {
            midlet = (MIDlet) midletClass.newInstance();
        } catch (Exception ex) {
            Logger.error("Cannot initialize " + midletClass + " MIDlet class", ex);
            return;
        }
        if (((DeviceDisplayImpl) device.getDeviceDisplay()).isResizable()) {
            resize(device.getDeviceDisplay().getFullWidth(), device.getDeviceDisplay().getFullHeight());
        } else {
            resize(device.getNormalImage().getWidth(), device.getNormalImage().getHeight());
        }
        return;
    }
