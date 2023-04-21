    public static void main(String[] argv) {
        {
            Handler handler = new Slf4JHandler();
            java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
            rootLogger.addHandler(handler);
            rootLogger.setLevel(java.util.logging.Level.WARNING);
        }
        String appTitle = APP_NAME + " V" + APP_VERSION;
        if (APP_SNAPSHOT) {
            appTitle = appTitle + " (" + APP_TIMESTAMP_DATE + ")";
        }
        CommandLineArgs cl = new CommandLineArgs();
        JCommander commander = new JCommander(cl);
        Cat cat = new Cat();
        commander.addCommand(Cat.NAME, cat);
        Tail tail = new Tail();
        commander.addCommand(Tail.NAME, tail);
        Index index = new Index();
        commander.addCommand(Index.NAME, index);
        Md5 md5 = new Md5();
        commander.addCommand(Md5.NAME, md5);
        Help help = new Help();
        commander.addCommand(Help.NAME, help);
        try {
            commander.parse(argv);
        } catch (ParameterException ex) {
            printAppInfo(appTitle, false);
            System.out.println(ex.getMessage() + "\n");
            printHelp(commander);
            System.exit(-1);
        }
        if (cl.verbose) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = new Date(APP_TIMESTAMP);
            appTitle += " - " + sdf.format(d);
            appTitle += " - " + APP_REVISION;
        }
        if (cl.showHelp) {
            printAppInfo(appTitle, false);
            printHelp(commander);
            System.exit(0);
        }
        String command = commander.getParsedCommand();
        if (!Tail.NAME.equals(command) && !Cat.NAME.equals(command)) {
            printAppInfo(appTitle, true);
        }
        if (cl.verbose) {
            initVerboseLogging();
        }
        if (cl.printBuildTimestamp) {
            System.out.println("Build-Timestamp: " + APP_TIMESTAMP);
            System.out.println("Build-Date     : " + APP_TIMESTAMP_DATE);
            System.out.println("Build-Revision : " + APP_REVISION);
            System.exit(0);
        }
        if (Help.NAME.equals(command)) {
            commander.usage();
            if (help.commands == null || help.commands.size() == 0) {
                commander.usage(Help.NAME);
            } else {
                Map<String, JCommander> commands = commander.getCommands();
                for (String current : help.commands) {
                    if (commands.containsKey(current)) {
                        commander.usage(current);
                    } else {
                        System.out.println("Unknown command '" + current + "'!");
                    }
                }
            }
            System.exit(0);
        }
        if (Md5.NAME.equals(command)) {
            List<String> files = md5.files;
            if (files == null || files.size() == 0) {
                printHelp(commander);
                System.exit(-1);
            }
            boolean error = false;
            for (String current : files) {
                if (!CreateMd5Command.createMd5(new File(current))) {
                    error = true;
                }
            }
            if (error) {
                System.exit(-1);
            }
            System.exit(0);
        }
        if (Index.NAME.equals(command)) {
            List<String> files = index.files;
            if (files == null || files.size() == 0) {
                printHelp(commander);
                System.exit(-1);
            }
            boolean error = false;
            for (String current : files) {
                if (!IndexCommand.indexLogFile(current, null)) {
                    error = true;
                }
            }
            if (error) {
                System.exit(-1);
            }
            System.exit(0);
        }
        if (Cat.NAME.equals(command)) {
            List<String> files = cat.files;
            if (files == null || files.size() != 1) {
                printHelp(commander);
                System.exit(-1);
            }
            if (CatCommand.catFile(new File(files.get(0)), cat.pattern, cat.numberOfLines)) {
                System.exit(0);
            }
            System.exit(-1);
        }
        if (Tail.NAME.equals(command)) {
            List<String> files = tail.files;
            if (files == null || files.size() != 1) {
                printHelp(commander);
                System.exit(-1);
            }
            if (TailCommand.tailFile(new File(files.get(0)), tail.pattern, tail.numberOfLines, tail.keepRunning)) {
                System.exit(0);
            }
            System.exit(-1);
        }
        if (cl.flushPreferences) {
            flushPreferences();
        }
        if (cl.exportPreferencesFile != null) {
            exportPreferences(cl.exportPreferencesFile);
        }
        if (cl.importPreferencesFile != null) {
            importPreferences(cl.importPreferencesFile);
        }
        if (cl.exportPreferencesFile != null || cl.importPreferencesFile != null) {
            System.exit(0);
        }
        if (cl.flushLicensed) {
            flushLicensed();
        }
        startLilith(appTitle, cl.enableBonjour);
    }
