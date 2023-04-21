    private synchronized void loadMyOptions() {
        socksProxyHost = "";
        socksProxyPort = 0;
        socksProxyUsername = "";
        socksProxyPassword = "";
        isDownloadThroughProxy = true;
        isSaveProxyPassword = false;
        networkEncoding = "UTF-8";
        lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        webHome = "phosphor.sourceforge.net";
        emailFeedback = "phosphor@turingcomplete.net";
        serverConnectOnStartup = true;
        numServersAutoConnectTo = 3;
        connectOnStartup = true;
        connectingInterfacePort = 0;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            Trace.display(e, "Fatal error: unable to find local host");
            System.exit(ExitCodes.UNRECOVERABLE_ERROR);
        }
        myPropertyFile.clear();
        try {
            myPropertyFile.load(new FileInputStream(myPropertyFileName));
        } catch (IOException e) {
            Trace.display(e, "Config file not found - using defaults.");
        }
        String workingDir = myPropertyFile.getProperty("workingDir");
        if (workingDir == null) {
            if (System.getProperty("javawebstart.version") != null) {
                File dir = new File(System.getProperty("user.home") + File.separatorChar + ProtocolDetails.PROGRAM_NAME + (ProtocolDetails.DEBUG ? ('_' + ProtocolDetails.VERSION_DASHES) : "") + File.separatorChar);
                if (dir.exists() || dir.mkdirs()) {
                    setWorkingDir(dir.getAbsoluteFile().getPath());
                } else {
                    Trace.display("Unable to create working directory");
                    setWorkingDir(System.getProperty("user.home"));
                }
            } else {
                setWorkingDir(System.getProperty("user.dir"));
            }
        }
        readTracing();
        if (myPropertyFileCipher != null) {
            String encPass = myPropertyFile.getProperty("socksProxyPassword");
            if (encPass != null) {
                try {
                    myPropertyFileCipher.init(Cipher.DECRYPT_MODE, myCipherKey, myCipherParams);
                    byte[] pass = myPropertyFileCipher.doFinal(getBytes(encPass));
                    String digest = getString(myMessageDigester.digest(pass));
                    if (digest.equals(get("socksProxyPasswordHash", ""))) {
                        socksProxyPassword = getString(pass);
                    } else {
                        Trace.display("socksProxyPassword not correct");
                    }
                } catch (GeneralSecurityException e) {
                    Trace.display(e, "Unable to decrypt socks password.");
                } catch (IllegalStateException e) {
                    Trace.display(e, "Unable to decrypt socks password.");
                }
            }
        }
        try {
            initSocksProxy();
        } catch (IOException e) {
            Trace.display(e, "Cannot initialize socks proxy");
        }
    }
