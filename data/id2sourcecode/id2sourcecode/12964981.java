    public static void main(String[] args) throws java.io.IOException {
        List<String> MCMCargs = new ArrayList<String>();
        Arguments arguments = new Arguments(new Arguments.Option[] { new Arguments.Option("window", "Provide a console window"), new Arguments.Option("options", "Display an options dialog"), new Arguments.Option("working", "Change working directory to input file's directory"), new Arguments.LongOption("seed", "Specify a random number generator seed"), new Arguments.StringOption("prefix", "PREFIX", "Specify a prefix for all output log filenames"), new Arguments.Option("overwrite", "Allow overwriting of log files"), new Arguments.Option("resume", "Allow appending of log files"), new Arguments.IntegerOption("errors", "Specify maximum number of numerical errors before stopping"), new Arguments.IntegerOption("threads", "The number of computational threads to use (default auto)"), new Arguments.Option("help", "Print this information and stop") });
        try {
            arguments.parseArguments(args);
        } catch (Arguments.ArgumentException ae) {
            System.out.println();
            System.out.println(ae.getMessage());
            System.out.println();
            printUsage(arguments);
            System.exit(1);
        }
        if (arguments.hasOption("help")) {
            printUsage(arguments);
            System.exit(0);
        }
        final boolean window = arguments.hasOption("window");
        final boolean options = arguments.hasOption("options");
        final boolean working = arguments.hasOption("working");
        String fileNamePrefix = null;
        long seed = Randomizer.getSeed();
        int threadCount = 1;
        if (arguments.hasOption("prefix")) {
            fileNamePrefix = arguments.getStringOption("prefix");
        }
        if (arguments.hasOption("threads")) {
            threadCount = arguments.getIntegerOption("threads");
            if (threadCount < 0) {
                printTitle();
                System.err.println("The the number of threads should be >= 0");
                System.exit(1);
            }
        }
        if (arguments.hasOption("seed")) {
            seed = arguments.getLongOption("seed");
            if (seed <= 0) {
                printTitle();
                System.err.println("The random number seed should be > 0");
                System.exit(1);
            }
        }
        int maxErrorCount = 0;
        if (arguments.hasOption("errors")) {
            maxErrorCount = arguments.getIntegerOption("errors");
            if (maxErrorCount < 0) {
                maxErrorCount = 0;
            }
        }
        BeastConsoleApp consoleApp = null;
        String nameString = "SNAPP " + version.getVersionString();
        if (window) {
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.showGrowBox", "true");
            System.setProperty("beast.useWindow", "true");
            javax.swing.Icon icon = IconUtils.getIcon(SNAPPMCMC.class, "snapp.png");
            String aboutString = "<html><div style=\"font-family:sans-serif;\"><center>" + "<div style=\"font-size:12;\"><p>Bayesian Evolutionary Analysis Sampling Trees<br>" + "Version " + version.getVersionString() + ", " + version.getDateString() + "</p>" + version.getHTMLCredits() + "</div></center></div></html>";
            consoleApp = new BeastConsoleApp(nameString, aboutString, icon);
        }
        printTitle();
        File inputFile = null;
        if (options) {
            String titleString = "<html><center><p>SNAPP<br>" + "Version " + version.getVersionString() + ", " + version.getDateString() + "</p></center></html>";
            javax.swing.Icon icon = IconUtils.getIcon(SNAPPMCMC.class, "snapp.png");
            SNAPPDialog dialog = new SNAPPDialog(new JFrame(), titleString, icon);
            if (!dialog.showDialog(nameString, seed)) {
                System.exit(0);
            }
            switch(dialog.getLogginMode()) {
                case 0:
                    break;
                case 1:
                    MCMCargs.add("-overwrite");
                    break;
                case 2:
                    MCMCargs.add("-resume");
                    break;
            }
            seed = dialog.getSeed();
            threadCount = dialog.getThreadPoolSize();
            inputFile = dialog.getInputFile();
        } else {
            if (arguments.hasOption("overwrite")) {
                MCMCargs.add("-overwrite");
            } else if (arguments.hasOption("resume")) {
                MCMCargs.add("-resume");
            }
        }
        if (inputFile == null) {
            String[] args2 = arguments.getLeftoverArguments();
            if (args2.length > 1) {
                System.err.println("Unknown option: " + args2[1]);
                System.err.println();
                printUsage(arguments);
                return;
            }
            String inputFileName = null;
            if (args2.length > 0) {
                inputFileName = args2[0];
                inputFile = new File(inputFileName);
            }
            if (inputFileName == null) {
                inputFile = new File(getFileNameByDialog("SNAPP " + version.getVersionString() + " - Select XML input file"));
            }
        }
        if (inputFile != null && inputFile.getParent() != null && working) {
            System.setProperty("user.dir", inputFile.getParent());
        }
        if (window) {
            if (inputFile == null) {
                consoleApp.setTitle("null");
            } else {
                consoleApp.setTitle(inputFile.getName());
            }
        }
        if (fileNamePrefix != null && fileNamePrefix.trim().length() > 0) {
            System.setProperty("file.name.prefix", fileNamePrefix.trim());
        }
        if (threadCount > 0) {
            System.setProperty("thread.count", String.valueOf(threadCount));
            MCMCargs.add("-threads");
            MCMCargs.add(threadCount + "");
        } else {
            MCMCargs.add("-threads");
            MCMCargs.add(Runtime.getRuntime().availableProcessors() + "");
        }
        MCMCargs.add("-seed");
        MCMCargs.add(seed + "");
        Randomizer.setSeed(seed);
        System.out.println();
        System.out.println("Random number seed: " + seed);
        System.out.println();
        BeastMCMC beastMCMC = new BeastMCMC();
        try {
            MCMCargs.add(inputFile.getAbsolutePath());
            beastMCMC.parseArgs(MCMCargs.toArray(new String[0]));
            new SNAPPMCMC(beastMCMC, consoleApp, maxErrorCount);
        } catch (RuntimeException rte) {
            if (window) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println();
                System.out.println("SNAPP has terminated with an error. Please select QUIT from the menu.");
            }
        } catch (XMLParserException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!window) {
            System.exit(0);
        }
    }
