    private void loadInputData(PrintWriter log_writer) {
        File inputFile;
        BufferedReader inputDataReader;
        boolean EOF = false;
        String line;
        cardBackLabels = new ArrayList();
        cardBackFiles = new ArrayList();
        tableImgLabels = new ArrayList();
        tableImgFiles = new ArrayList();
        try {
            if (log_writer != null) {
                log_writer.println("Loading inputs from " + INPUT_FNAME);
            }
            if (isApplet) {
                ClassLoader cl = getClass().getClassLoader();
                java.net.URL url = cl.getResource(INPUT_FNAME);
                inputDataReader = new BufferedReader(new InputStreamReader(url.openStream()));
            } else {
                inputFile = new File(INPUT_FNAME);
                inputDataReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            }
            boolean readingRules = false;
            boolean readingGames = false;
            boolean readingAI = false;
            boolean readingCardBacks = false;
            boolean readingTableImgs = false;
            boolean readingClientOpts = false;
            boolean readingAppletOpts = false;
            boolean readingDealer = false;
            boolean readingLogging = false;
            while (!EOF) {
                try {
                    line = inputDataReader.readLine();
                    if (!line.matches("^#.*")) {
                        if (readingCardBacks) {
                            int i = line.indexOf(',');
                            cardBackLabels.add(line.substring(0, i).trim());
                            cardBackFiles.add(line.substring(i + 1).trim());
                        }
                        if (readingTableImgs) {
                            int i = line.indexOf(',');
                            tableImgLabels.add(line.substring(0, i).trim());
                            tableImgFiles.add(line.substring(i + 1).trim());
                        }
                        if (readingClientOpts) {
                            int i = line.indexOf('=');
                            String option = line.substring(0, i).trim();
                            String value = line.substring(i + 1).trim();
                            if (option.equals("hideHoleCards")) {
                                hideHoleCards = value.equals("true");
                            }
                            if (option.equals("muckLosingHands")) {
                                muckLosingHands = value.equals("true");
                            }
                            if (option.equals("oneClickCheckCall")) {
                                oneClickCheckCall = value.equals("true");
                            }
                        }
                        if (readingAppletOpts) {
                            int i = line.indexOf('=');
                            String option = line.substring(0, i).trim();
                            String value = line.substring(i + 1).trim();
                            if (option.equals("server")) {
                                int j = value.indexOf(':');
                                appletServer = value.substring(0, j).trim();
                                appletPort = value.substring(j + 1).trim();
                            }
                        }
                        if (readingLogging) {
                            int i = line.indexOf('=');
                            String var = line.substring(0, i).trim();
                            String val = line.substring(i + 1).trim();
                            if (var.equals("logging")) {
                                logging = val.equals("true");
                            } else if (var.equals("keepLogFile")) {
                                keepLogFile = val.equals("true");
                            } else if (var.equals("logLevel")) {
                                try {
                                    logLevel = Integer.parseInt(val);
                                    if (logging) {
                                        createLogFile();
                                    }
                                } catch (Exception x) {
                                    System.out.println("Warning : Could not set logLevel.");
                                    if (log_writer != null) {
                                        log_writer.println("Warning : Could not set logLevel.");
                                    }
                                    x.printStackTrace();
                                }
                            } else {
                                System.out.println("Warning : Bad variable in input file in the LOGGING block.");
                                if (log_writer != null) {
                                    log_writer.println("Warning : Bad variable in input file in the LOGGING block.");
                                }
                            }
                        }
                    } else {
                        if (line.matches(".*RULES")) {
                            readingRules = true;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*GAMES")) {
                            readingRules = false;
                            readingGames = true;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*AI")) {
                            readingRules = false;
                            readingGames = false;
                            readingAI = true;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*CARDBACKS")) {
                            if (log_writer != null) {
                                log_writer.println("  Reading card images");
                            }
                            readingRules = false;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = true;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*TABLEIMGS")) {
                            if (log_writer != null) {
                                log_writer.println("  Reading table images");
                            }
                            readingRules = false;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = true;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*CLIENTOPTIONS")) {
                            if (log_writer != null) {
                                log_writer.println("  Reading client options");
                            }
                            readingRules = false;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = true;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*APPLETOPTIONS")) {
                            readingRules = false;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = true;
                            readingDealer = false;
                            readingLogging = false;
                        }
                        if (line.matches(".*DEALER")) {
                            readingRules = false;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = true;
                            readingLogging = false;
                        }
                        if (line.matches(".*LOGGING")) {
                            if (log_writer != null) {
                                log_writer.println("  Reading logging variables");
                            }
                            readingRules = false;
                            readingGames = false;
                            readingAI = false;
                            readingCardBacks = false;
                            readingTableImgs = false;
                            readingClientOpts = false;
                            readingAppletOpts = false;
                            readingDealer = false;
                            readingLogging = true;
                        }
                    }
                } catch (NullPointerException x) {
                    EOF = true;
                } catch (IOException x) {
                    EOF = true;
                }
            }
        } catch (FileNotFoundException x) {
            displayError("File Read Error", "Critical Error reading input file: " + INPUT_FNAME, true);
            if (log_writer != null) {
                log_writer.println("ERROR - reading input file: " + INPUT_FNAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
