    public static void main(String[] args) {
        Debug.init();
        Debug.enable();
        Debug.log("Main", "Palantir version " + progVersion + " starting");
        Debug.logEnvironment();
        Debug.disable();
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("--debug")) {
                Debug.enable();
                delay = 120;
                initialDelay = 5;
                Debug.log("Main", "Running tests at " + delay + " seconds interval, starting in " + initialDelay + " seconds");
            }
        }
        try {
            ServerSocket s = new ServerSocket(55555);
        } catch (Exception e) {
            System.err.println("Another instance is allready running");
            System.exit(0);
        }
        initConfig();
        logC.writeToLog(0, "Starting Palantir");
        ActionListener exitListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        if (SystemTray.isSupported()) {
            gui.setVisible(false);
            Debug.log("Main", "creating trayicon");
            trayIcon = new TrayIcon(trayImg, "Palantir", popup);
            MouseAdapter mouseListener = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (gui.isVisible()) {
                            gui.setVisible(false);
                        } else {
                            gui.setVisible(true);
                        }
                    }
                }
            };
            trayIcon.addMouseListener(mouseListener);
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(exitListener);
            popup.add(exit);
            trayIcon.setImageAutoSize(true);
            try {
                tray = SystemTray.getSystemTray();
                tray.add(trayIcon);
            } catch (Exception e) {
                logC.writeToLog(2, "Unable to add the trayicon");
            }
        } else {
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setVisible(true);
            logC.writeToLog(2, "Systemtray is not Supported on this platform");
        }
        validUserdata = checkCredentials();
        testClock.schedule(new TimerTask() {

            public void run() {
                Debug.log("Tooltip Updater", "Starting run");
                if (testVar) {
                    int tempTime = nextTime;
                    int hr = tempTime / 3600;
                    tempTime -= 3600 * hr;
                    int min = tempTime / 60;
                    tempTime -= 60 * min;
                    int sec = tempTime;
                    nextTest = "Next Test in " + hr + ":" + min + ":" + sec;
                } else {
                    nextTest = "Tests Disabled";
                }
                nextTime -= 5;
                if (nextTime <= 0) {
                    nextTest = "Running Tests";
                }
                getUsername();
                String dbgEnabled = Debug.isEnabled() ? " [Debug]" : "";
                String userstr = user.equalsIgnoreCase("") ? "No username" : "Username: " + user;
                trayIcon.setToolTip("Palantir - " + nextTest + " - " + userstr + dbgEnabled);
                gui.setTitle("Palantir - " + nextTest + " - " + userstr + dbgEnabled);
            }
        }, 0, 5000);
        rss.schedule(new TimerTask() {

            ArrayList<HashMap> feed = new ArrayList<HashMap>();

            ArrayList<HashMap> newFeed = new ArrayList<HashMap>();

            ArrayList<HashMap> tempFeed = new ArrayList<HashMap>();

            public void run() {
                Debug.log("RSS Updater", "Starting run");
                logC.writeToLog(0, "Attempting RSS Read");
                if (rssVar) {
                    Debug.log("RSS Updater", "rss variables read succesfully from var.xml");
                    try {
                        Debug.log("RSS Updater", "reading local rss.xml file");
                        feed = rssC.getLocalFeed(rssXmlFile);
                    } catch (Exception ex) {
                        Debug.log("RSS Updater", "no local rss feed found");
                        logC.writeToLog(0, "No local feed found, trying online.");
                        feed = tempFeed;
                    }
                    try {
                        Debug.log("RSS Updater", "reading rss feed online");
                        newFeed = rssC.getFeed();
                    } catch (Exception e) {
                        Debug.log("RSS Updater", "online rss feed read failed");
                        logC.writeToLog(2, "Unable to read online feed.");
                    }
                    Debug.log("RSS Updater", "comparing old and new feed");
                    if (!feed.equals(newFeed)) {
                        if (!newFeed.isEmpty()) {
                            trayIcon.displayMessage("News!", "There is a new newspost", TrayIcon.MessageType.INFO);
                            try {
                                Debug.log("RSS Updater", "writing new feed to local xml file");
                                rssC.feedToLocalXML(newFeed, rssXmlFile);
                            } catch (Exception exx) {
                                Debug.log("RSS Updater", "local feed file write failed, keeping copy in memory");
                                tempFeed = newFeed;
                            }
                        }
                        Debug.log("RSS Updater", "updating gui news tab");
                        gui.setNewsTab();
                    } else {
                        if (feed.isEmpty()) {
                            gui.setNewsTab("Unable to load the feed");
                        } else {
                            Debug.log("RSS Updater", "updating gui news tab");
                            gui.setNewsTab();
                        }
                    }
                }
            }
        }, rssInitialDelay * 1000, rssDelay * 1000);
        t.schedule(new TimerTask() {

            public void run() {
                Debug.log("Tests", "Starting run");
                logC.writeToLog(0, "Attempting to run tests");
                if (testVar) {
                    Debug.log("Tests", "test variables read succesfully");
                    internet = false;
                    Debug.log("Tests", "executing mastertest");
                    for (int i = 0; i < masterTest.length; i++) {
                        try {
                            Debug.log("Tests", "Mastertest: trying to connect to " + masterTest[i]);
                            Socket s = new Socket();
                            InetAddress addr = InetAddress.getByName(masterTest[i]);
                            SocketAddress sockAddr = new InetSocketAddress(addr, 21);
                            s.connect(sockAddr, 300);
                            s.close();
                            Debug.log("Tests", "Mastertest: connect to " + masterTest[i] + " succesful. Enabling other tests");
                            internet = true;
                        } catch (Exception e) {
                            Debug.log("Tests", "Mastertest to " + masterTest[i] + " Failed");
                            logC.writeToLog(1, "Mastertest to " + masterTest[i] + " Failed");
                        }
                    }
                    if (internet) {
                        double latestVersion = 0.0;
                        try {
                            Debug.log("Tests", "Checking for new client version");
                            latestVersion = Double.parseDouble(xmlRpcC.getLatestVersion());
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }
                        if (latestVersion > progVersion) {
                            Debug.log("Tests", "Current version (" + progVersion + ") is older than latest version (" + latestVersion + ")");
                            trayIcon.displayMessage("New Version!", "There is a new version available.\n Please Upgrade.", TrayIcon.MessageType.ERROR);
                        } else {
                            Debug.log("Tests", "Current version (" + progVersion + ") is up to date with latest version (" + latestVersion + ")");
                        }
                        try {
                            Debug.log("Tests", "checking for new configfile");
                            logC.writeToLog(0, "Checking for new configfile");
                            CfgUpdater cfgUpdater = new CfgUpdater();
                            cfgUpdater.update(updateServer + configFile, xmlC.readCfgVersion(homeDir + configFile));
                            if (cfgUpdater.getResult() != null) {
                                trayIcon.displayMessage("Updater", cfgUpdater.getResult(), TrayIcon.MessageType.INFO);
                            }
                        } catch (Exception e) {
                            Debug.log("Tests", "unable to retrieve new configfile, checking for existing (old) one");
                            if (new File(homeDir + configFile).exists()) {
                                logC.writeToLog(2, "Unable to retrieve a new configfile, using existing version (" + homeDir + configFile + ")");
                                cfgFileExists = true;
                            } else {
                                Debug.log("Tests", "no existing configfile found");
                                cfgFileExists = false;
                                logC.writeToLog(1, "No config file exists and unable to retrieve a new one");
                            }
                        }
                        if (cfgFileExists && validUserdata) {
                            Debug.log("Tests", "running tests");
                            logC.writeToLog(0, "Running tests");
                            Debug.log("Tests", "reading configfile for tests");
                            testParamsArray = xmlC.readTests(homeDir + configFile);
                            Debug.log("Tests", "creating array of tests");
                            tests = testC.createTests(testParamsArray);
                            Debug.log("Tests", "Created array with " + tests.size() + " tests");
                            Iterator itTests = tests.iterator();
                            int testCounter = 1;
                            Debug.log("Tests", "Looping thru " + tests.size() + " tests found in the configfile");
                            while (itTests.hasNext()) {
                                String prefix = "[" + testCounter + "/" + tests.size() + "] ";
                                iTest test = (iTest) itTests.next();
                                Debug.log("Tests", prefix + "Reading old unsent results");
                                resultArray = xmlC.readResults(homeDir + resultsFile);
                                int aResults = resultArray.size();
                                Debug.log("Tests", prefix + "Found " + aResults + " unsent results");
                                Debug.log("Tests", prefix + "Executing test with description: " + test);
                                resultArray = testC.execTest(test, resultArray);
                                int diff = resultArray.size() - aResults;
                                Debug.log("Tests", prefix + "Test resulted in " + diff + " new results, total unset now: " + resultArray.size());
                                for (int i = diff; i != 0; i--) {
                                    Debug.log("Tests", prefix + "updating gui test tab");
                                    gui.setTestTabText(resultArray.get(resultArray.size() - i));
                                }
                                Debug.log("Tests", prefix + "attempting to send the results to the server");
                                Iterator rIt = resultArray.iterator();
                                while (rIt.hasNext()) {
                                    HashMap result = (HashMap) rIt.next();
                                    try {
                                        String userdata = xmlC.readUserdata(userdataFile);
                                        Debug.log("Tests", prefix + "reading userdata to authenticate with the server");
                                        String[] udata = userdata.split("/");
                                        result.put("username", udata[0]);
                                        result.put("passhash", udata[1]);
                                        result.put("progversion", progVersion);
                                        Debug.log("Tests", prefix + "sending result to the server");
                                        xmlRpcC.storeResult(result);
                                        rIt.remove();
                                        Debug.log("Tests", prefix + "write remaining results to xml file");
                                        xmlC.writeResults(resultArray, homeDir + resultsFile);
                                        Debug.log("Tests", prefix + "call done");
                                    } catch (Exception ex) {
                                        logC.writeToLog(1, "Unable to send results, saving local");
                                        Debug.log("Tests", prefix + "sending of result failed");
                                        Debug.log("Tests", ex.toString());
                                        Debug.log("Tests", prefix + "call failed");
                                        if (ex.getMessage().toLowerCase().contains("database") || ex.getMessage().toLowerCase().contains("query")) {
                                            logC.writeToLog(1, "Database Problem, try again later");
                                            Debug.log("Tests", prefix + "Database Problem, try again later");
                                        } else {
                                            if (ex.getMessage().toLowerCase().contains("invalid userinfo")) {
                                                new File(userdataFile).delete();
                                                logC.writeToLog(2, "No valid userdata found");
                                                Debug.log("Tests", prefix + "No valid userdata found");
                                            }
                                        }
                                        Debug.log("Tests", prefix + "write results to xml file");
                                        xmlC.writeResults(resultArray, homeDir + resultsFile);
                                    }
                                }
                                cleanUp();
                                testCounter++;
                            }
                            tests.clear();
                            Debug.log("Tests", "All tests executed. Sleeping untill next invocation");
                        } else {
                            Debug.log("Tests", "unable to retrieve configfile/no existing one found. Waiting for next attempt");
                            logC.writeToLog(1, "No configuration file was found, unable to retrieve new file");
                            trayIcon.displayMessage("Configuration Error", "No configuration file was found - Unable to contact the updateserver - Try again later", TrayIcon.MessageType.ERROR);
                        }
                    }
                }
                nextTime = delay;
            }

            public void cleanUp() {
                Debug.log("Tests", "cleanup the previous tests");
                testParamsArray.clear();
                resultArray.clear();
            }
        }, initialDelay, delay * 1000);
    }
