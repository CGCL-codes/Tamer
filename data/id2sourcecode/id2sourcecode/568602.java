    public SkyRouterClient(String[] argsI) {
        try {
            ArgHandler ah = new ArgHandler(argsI);
            if (ah.checkFlag('a')) {
                String addressL = ah.getOption('a');
                if (addressL != null) {
                    address = addressL;
                } else {
                    System.err.println("WARNING: Null argument to the \"-a\"" + " command line option.");
                }
            }
            if (ah.checkFlag('c')) {
                try {
                    String framesStr = ah.getOption('c');
                    if (framesStr != null) {
                        cacheFrames = Integer.parseInt(framesStr);
                        if (cacheFrames <= 0) {
                            System.err.println("ERROR: The cache frames specified with " + "the \"-c\" flag must be an integer greater " + "than 0");
                            bImmediateShutdown = true;
                            System.exit(0);
                        }
                    } else {
                        System.err.println("WARNING: Null argument to the \"-c\"" + " command line option.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("ERROR: The cache frames specified with the " + "\"-c\" flag is not a number.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            }
            if (ah.checkFlag('h')) {
                System.err.println("SkyRouterClient command line options");
                System.err.println("   -a <RBNB address>");
                System.err.println("       default: " + address);
                System.err.println("   -c <cache frames>");
                System.err.println("       default: " + cacheFrames + " frames");
                System.err.println("   -h (display this help message)");
                System.err.println("   -I <IMEI number>");
                System.err.println("       default: none; this is a required " + "argument");
                System.err.println("   -k <archive frames>");
                System.err.println("       default: " + archiveFrames + " frames, append archive");
                System.err.println("   -K <archive frames>");
                System.err.println("       default: " + archiveFrames + " frames, create archive");
                System.err.println("   -n <output source name>");
                System.err.println("       default: " + sourceName);
                System.err.println("   -o (Catchup on older/missing data)");
                System.err.println("   -p <password>");
                System.err.println("       default: none; this is a required " + "argument");
                System.err.println("   -t <poll period, in seconds>");
                System.err.println("       default: " + pollPeriod);
                System.err.println("   -u <username>");
                System.err.println("       default: none; this is a required " + "argument");
                System.err.println("   -v (Verbose mode)");
                bImmediateShutdown = true;
                System.exit(0);
            }
            if (ah.checkFlag('I')) {
                try {
                    String imeiStr = ah.getOption('I');
                    if (imeiStr != null) {
                        imeiNum = Long.parseLong(imeiStr);
                        if (imeiNum <= 0) {
                            System.err.println("ERROR: The IMEI number specified with " + "the \"-I\" flag must be an integer greater " + "than 0");
                            bImmediateShutdown = true;
                            System.exit(0);
                        }
                    } else {
                        System.err.println("ERROR: Must provide the IMEI number with the " + "\"-I\" command line option.");
                        bImmediateShutdown = true;
                        System.exit(0);
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("ERROR: The IMEI number specified with the " + "\"-I\" flag is not a number.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            } else {
                System.err.println("ERROR: Must provide the IMEI number with the \"-I\" " + "command line option.");
                bImmediateShutdown = true;
                System.exit(0);
            }
            if (ah.checkFlag('k')) {
                try {
                    String framesStr = ah.getOption('k');
                    if (framesStr != null) {
                        archiveFrames = Integer.parseInt(framesStr);
                        if (archiveFrames <= 0) {
                            System.err.println("ERROR: The archive frames specified with " + "the \"-k\" flag must be an integer greater " + "than 0");
                            bImmediateShutdown = true;
                            System.exit(0);
                        }
                        archiveMode = new String("append");
                    } else {
                        System.err.println("WARNING: Null argument to the \"-k\"" + " command line option.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("ERROR: The archive frames specified with the " + "\"-k\" flag is not a number.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            }
            if (ah.checkFlag('K')) {
                try {
                    String framesStr = ah.getOption('K');
                    if (framesStr != null) {
                        archiveFrames = Integer.parseInt(framesStr);
                        if (archiveFrames <= 0) {
                            System.err.println("ERROR: The archive frames specified with " + "the \"-K\" flag must be an integer greater " + "than 0");
                            bImmediateShutdown = true;
                            System.exit(0);
                        }
                        archiveMode = new String("create");
                    } else {
                        System.err.println("WARNING: Null argument to the \"-K\"" + " command line option.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("ERROR: The archive frames specified with the " + "\"-K\" flag is not a number.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            }
            if (ah.checkFlag('n')) {
                String sourceNameL = ah.getOption('n');
                if (sourceNameL != null) {
                    sourceName = sourceNameL;
                } else {
                    System.err.println("WARNING: Null argument to the \"-n\"" + " command line option.");
                }
            }
            if (ah.checkFlag('o')) {
                bCatchup = true;
            }
            if (ah.checkFlag('p')) {
                String passwordL = ah.getOption('p');
                if (passwordL != null) {
                    password = passwordL;
                } else {
                    System.err.println("ERROR: Must provide a password with the \"-p\" " + "command line option.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            } else {
                System.err.println("ERROR: Must provide a password with the \"-p\" command " + "line option.");
                bImmediateShutdown = true;
                System.exit(0);
            }
            if (ah.checkFlag('t')) {
                try {
                    String pollPeriodStr = ah.getOption('t');
                    if (pollPeriodStr != null) {
                        pollPeriod = Integer.parseInt(pollPeriodStr);
                        if (pollPeriod < MIN_POLL_PERIOD) {
                            System.err.println("ERROR: The poll period specified with the " + "\"-t\" flag must be an integer greater than " + MIN_POLL_PERIOD);
                            bImmediateShutdown = true;
                            System.exit(0);
                        }
                    } else {
                        System.err.println("WARNING: Null argument to the \"-t\"" + " command line option.");
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("ERROR: The poll period specified with the " + "\"-t\" flag is not a number.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            }
            if (ah.checkFlag('u')) {
                String usernameL = ah.getOption('u');
                if (usernameL != null) {
                    username = usernameL;
                } else {
                    System.err.println("ERROR: Must provide a username with the \"-u\" command line option.");
                    bImmediateShutdown = true;
                    System.exit(0);
                }
            } else {
                System.err.println("ERROR: Must provide a username with the \"-u\" command line option.");
                bImmediateShutdown = true;
                System.exit(0);
            }
            if (ah.checkFlag('v')) {
                bVerbose = true;
            }
        } catch (Exception e) {
            System.err.println("SkyRouterClient argument exception " + e.getMessage());
            e.printStackTrace();
            bImmediateShutdown = true;
            System.exit(0);
        }
        System.err.println("\nArguments:");
        System.err.println("RBNB address: " + address);
        System.err.println("RBNB source: " + sourceName);
        System.err.println("Cache frames: " + cacheFrames);
        if (archiveFrames == 0) {
            System.err.println("No archive");
        } else {
            System.err.println("Archive frames: " + archiveFrames);
            System.err.println("Archive mode: " + archiveMode);
        }
        System.err.println("Poll period: " + pollPeriod + " sec");
        if (bCatchup) {
            System.err.println("Catchup on older/missing data");
        } else {
            System.err.println("Ignore older/missing data");
        }
        System.err.println("IMEI number: " + imeiNum);
        MyShutdownHook shutdownHook = new MyShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        try {
            source = new Source(cacheFrames, archiveMode, archiveFrames);
            source.OpenRBNBConnection(address, sourceName);
            System.err.println("\nOpened RBNB connection to " + source.GetServerName() + ", source = " + source.GetClientName() + "\n");
        } catch (SAPIException e) {
            System.err.println(e);
            bImmediateShutdown = true;
            System.exit(0);
        }
        long startRequestTime = System.currentTimeMillis();
        if (bCatchup) {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.set(2010, 01, 01, 00, 00, 00);
            startRequestTime = cal.getTimeInMillis();
            ChannelMap dataMap = null;
            try {
                ChannelMap cm = new ChannelMap();
                cm.Add(new String(source.GetClientName() + "/" + imeiNum + "/" + "Lat"));
                Sink sink = new Sink();
                sink.OpenRBNBConnection(address, "TmpSink");
                sink.Request(cm, 0.0, 0.0, "newest");
                dataMap = sink.Fetch(10000);
                sink.CloseRBNBConnection();
            } catch (SAPIException e) {
                System.err.println("Error trying to determine timestamp of most recent " + "data point.:");
                System.err.println(e);
                bImmediateShutdown = true;
                System.exit(0);
            }
            if (dataMap.NumberOfChannels() == 1) {
                startRequestTime = (long) (dataMap.GetTimeStart(0) * 1000.0);
                startRequestTime = startRequestTime + 1000;
            }
        }
        Date requestDate = new Date(startRequestTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'+'HH'%3A'mm'%3A'ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateStr = sdf.format(requestDate);
        String urlStr = new String("https://www.skyrouter.com/DataExchange/get.php?userid=" + username + "&pw=" + password + "&source=ft&cmd=since&since=" + dateStr);
        System.err.println("\nInitial \"since\" request URL = " + urlStr);
        try {
            URL skyRouterURL = new URL(urlStr);
            HttpURLConnection skyRouterCon = (HttpURLConnection) skyRouterURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(skyRouterCon.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (bVerbose) {
                    System.out.println(inputLine);
                }
                try {
                    String reportDateStr = processData(inputLine);
                    if (reportDateStr != null) {
                        System.err.println(reportDateStr);
                    }
                } catch (Exception e1) {
                    System.err.println("Caught exception processing message:\n" + e1);
                    e1.printStackTrace();
                }
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Caught exception with initial SkyRouter request:\n" + e);
            bImmediateShutdown = true;
            System.exit(0);
        }
        try {
            Thread.sleep(pollPeriod * 1000);
        } catch (Exception e) {
        }
        urlStr = new String("https://www.skyrouter.com/DataExchange/get.php?userid=" + username + "&pw=" + password + "&source=ft&cmd=last&since=");
        System.err.println("\nPolling \"last\" request URL = " + urlStr);
        while (bKeepRunning) {
            try {
                URL skyRouterURL = new URL(urlStr);
                HttpURLConnection skyRouterCon = (HttpURLConnection) skyRouterURL.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(skyRouterCon.getInputStream()));
                String inputLine;
                if (bVerbose) {
                    System.err.println("Checking for data...");
                }
                while ((inputLine = in.readLine()) != null) {
                    if (bVerbose) {
                        System.out.println(inputLine);
                    }
                    try {
                        String reportDateStr = processData(inputLine);
                        if (reportDateStr != null) {
                            System.err.println(reportDateStr);
                        }
                    } catch (Exception e1) {
                        System.err.println("Caught exception processing message:\n" + e1);
                        e1.printStackTrace();
                    }
                }
                in.close();
                Thread.sleep(pollPeriod * 1000);
            } catch (Exception e) {
                System.err.println("Caught exception with SkyRouter request:\n" + e);
                continue;
            }
        }
        source.CloseRBNBConnection();
        bShutdown = true;
    }
