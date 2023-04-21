    private synchronized void initializeFramework() {
        if (PropertiesLoader.isCPEnabled()) {
            KeyPair kp = ToolBox.generateKeyPair();
            CaptivePortalDialog dlg = new CaptivePortalDialog(null, true, "Captive Portal");
            dlg.setVisible(true);
            if (dlg.answer) {
                eu.popeye.security.accesscontrol.captiveportal.CaptivePortal.runCaptivePortal(dlg.username_, dlg.password_, dlg.email_, dlg.fullname_, "popai", true, kp);
            }
        }
        splash.setProgress(12, "Loading properties");
        File ldd = new File(PropertiesLoader.getLocalDataPath());
        if (ldd.exists() && !ldd.isDirectory()) {
            System.err.println("Cannot create directory!\nContinue at own risk ;)");
        }
        if (!ldd.exists()) {
            if (ldd.mkdirs()) {
                System.err.println("Local Data Path Created");
            } else {
                System.err.println("Local Data Path Creation Failed");
            }
        }
        splash.setProgress(16, "Initializing System");
        SystemInitialization.initialize();
        splash.setProgress(22, "Initializing User Management");
        usrManagement = UserManagement.getInstance();
        splash.setProgress(35, "Starting Context Management System");
        try {
            SecurityMsgServer.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR Could not start security msg server");
        }
    }
