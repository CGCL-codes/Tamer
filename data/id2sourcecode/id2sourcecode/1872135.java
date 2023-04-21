    private void loadInputData(PrintWriter log_writer) {
        File inputFile;
        BufferedReader inputDataReader;
        boolean EOF = false;
        String line;
        try {
            log_writer.println("Loading inputs from " + INPUT_FNAME);
            inputFile = new File(INPUT_FNAME);
            inputDataReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
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
                        if (readingRules) {
                            int i = line.indexOf('=');
                            ruleNames.add(line.substring(0, i).trim());
                            ruleValues.add(line.substring(i + 1).trim());
                        }
                        if (readingGames) {
                            int i = line.indexOf(',');
                            gameLabels.add(line.substring(0, i).trim());
                            gameClasses.add(line.substring(i + 1).trim());
                        }
                        if (readingAI) {
                            int i = line.indexOf(',');
                            aiLabels.add(line.substring(0, i).trim());
                            aiClasses.add(line.substring(i + 1).trim());
                        }
                        if (readingDealer) {
                            int i = line.indexOf('=');
                            if ((line.substring(0, i).trim()).equals("autoDealing")) {
                                ruleNames.add(line.substring(0, i).trim());
                                ruleValues.add(line.substring(i + 1).trim());
                            }
                            if ((line.substring(0, i).trim()).equals("dealGame")) {
                                dealingGames.add(line.substring(i + 1).trim());
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
                                    if (logging) createLogFile();
                                } catch (Exception x) {
                                    System.out.println("Warning : Could not set logLevel.");
                                    log_writer.println("Warning : Could not set logLevel.");
                                    x.printStackTrace();
                                }
                            } else {
                                System.out.println("Warning : Bad variable in input file in the LOGGING block.");
                                log_writer.println("Warning : Bad variable in input file in the LOGGING block.");
                            }
                        }
                    } else {
                        if (line.matches(".*RULES")) {
                            log_writer.println("  Reading rules");
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
                            log_writer.println("  Reading games available");
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
                            log_writer.println("  Reading AIs available");
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
                            log_writer.println("  Reading dealer variables");
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
                            log_writer.println("  Reading logging variables");
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
            System.out.println("ERROR - reading input file: " + INPUT_FNAME);
            log_writer.println("ERROR - reading input file: " + INPUT_FNAME);
            log_writer.close();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            log_writer.close();
            System.exit(1);
        }
    }
