    public static void main(String[] args) throws java.io.IOException {
        Locale.setDefault(Locale.US);
        Arguments arguments = new Arguments(new Arguments.Option[] { new Arguments.Option("verbose", "Give verbose XML parsing messages"), new Arguments.Option("warnings", "Show warning messages about BEAST XML file"), new Arguments.Option("strict", "Fail on non-conforming BEAST XML file"), new Arguments.Option("window", "Provide a console window"), new Arguments.Option("options", "Display an options dialog"), new Arguments.Option("working", "Change working directory to input file's directory"), new Arguments.LongOption("seed", "Specify a random number generator seed"), new Arguments.StringOption("prefix", "PREFIX", "Specify a prefix for all output log filenames"), new Arguments.Option("overwrite", "Allow overwriting of log files"), new Arguments.IntegerOption("errors", "Specify maximum number of numerical errors before stopping"), new Arguments.IntegerOption("threads", "The number of computational threads to use (default auto)"), new Arguments.Option("java", "Use Java only, no native implementations"), new Arguments.Option("beagle", "Use beagle library if available"), new Arguments.Option("beagle_info", "BEAGLE: show information on available resources"), new Arguments.StringOption("beagle_order", "order", "BEAGLE: set order of resource use"), new Arguments.IntegerOption("beagle_instances", "BEAGLE: divide site patterns amongst instances"), new Arguments.Option("beagle_CPU", "BEAGLE: use CPU instance"), new Arguments.Option("beagle_GPU", "BEAGLE: use GPU instance if available"), new Arguments.Option("beagle_SSE", "BEAGLE: use SSE extensions if available"), new Arguments.Option("beagle_single", "BEAGLE: use single precision if available"), new Arguments.Option("beagle_double", "BEAGLE: use double precision if available"), new Arguments.StringOption("beagle_scaling", new String[] { "default", "dynamic", "delayed", "always", "none" }, false, "BEAGLE: specify scaling scheme to use"), new Arguments.IntegerOption("beagle_rescale", "BEAGLE: frequency of rescaling (dynamic scaling only)"), new Arguments.Option("help", "Print this information and stop") });
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
        List<String> additionalParsers = new ArrayList<String>();
        final boolean verbose = arguments.hasOption("verbose");
        final boolean parserWarning = arguments.hasOption("warnings");
        final boolean strictXML = arguments.hasOption("strict");
        final boolean window = arguments.hasOption("window");
        final boolean options = arguments.hasOption("options");
        final boolean working = arguments.hasOption("working");
        String fileNamePrefix = null;
        boolean allowOverwrite = arguments.hasOption("overwrite");
        long seed = MathUtils.getSeed();
        boolean useJava = false;
        int threadCount = 0;
        if (arguments.hasOption("java")) {
            useJava = true;
        }
        if (arguments.hasOption("prefix")) {
            fileNamePrefix = arguments.getStringOption("prefix");
        }
        long beagleFlags = 0;
        boolean useBeagle = arguments.hasOption("beagle") || arguments.hasOption("beagle_CPU") || arguments.hasOption("beagle_GPU") || arguments.hasOption("beagle_SSE") || arguments.hasOption("beagle_double") || arguments.hasOption("beagle_single") || arguments.hasOption("beagle_order") || arguments.hasOption("beagle_scaling") || arguments.hasOption("beagle_rescale") || arguments.hasOption("beagle_instances");
        boolean beagleShowInfo = arguments.hasOption("beagle_info");
        if (arguments.hasOption("beagle_CPU")) {
            beagleFlags |= BeagleFlag.PROCESSOR_CPU.getMask();
        }
        if (arguments.hasOption("beagle_GPU")) {
            beagleFlags |= BeagleFlag.PROCESSOR_GPU.getMask();
        }
        if (arguments.hasOption("beagle_SSE")) {
            beagleFlags |= BeagleFlag.PROCESSOR_CPU.getMask();
            beagleFlags |= BeagleFlag.VECTOR_SSE.getMask();
        }
        if (arguments.hasOption("beagle_double")) {
            beagleFlags |= BeagleFlag.PRECISION_DOUBLE.getMask();
        }
        if (arguments.hasOption("beagle_single")) {
            beagleFlags |= BeagleFlag.PRECISION_SINGLE.getMask();
        }
        if (arguments.hasOption("beagle_order")) {
            System.setProperty("beagle.resource.order", arguments.getStringOption("beagle_order"));
        }
        if (arguments.hasOption("beagle_instances")) {
            System.setProperty("beagle.instance.count", Integer.toString(arguments.getIntegerOption("beagle_instances")));
        }
        if (arguments.hasOption("beagle_scaling")) {
            System.setProperty("beagle.scaling", arguments.getStringOption("beagle_scaling"));
        }
        if (arguments.hasOption("beagle_rescale")) {
            System.setProperty("beagle.rescale", Integer.toString(arguments.getIntegerOption("beagle_rescale")));
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
        String nameString = "BEAST " + version.getVersionString();
        if (window) {
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.showGrowBox", "true");
            javax.swing.Icon icon = IconUtils.getIcon(BeastMain.class, "images/beast.png");
            String aboutString = "<html><div style=\"font-family:sans-serif;\"><center>" + "<div style=\"font-size:12;\"><p>Bayesian Evolutionary Analysis Sampling Trees<br>" + "Version " + version.getVersionString() + ", " + version.getDateString() + "</p>" + version.getHTMLCredits() + "</div></center></div></html>";
            consoleApp = new BeastConsoleApp(nameString, aboutString, icon);
        }
        printTitle();
        File inputFile = null;
        if (options) {
            String titleString = "<html><center><p>Bayesian Evolutionary Analysis Sampling Trees<br>" + "Version " + version.getVersionString() + ", " + version.getDateString() + "</p></center></html>";
            javax.swing.Icon icon = IconUtils.getIcon(BeastMain.class, "images/beast.png");
            BeastDialog dialog = new BeastDialog(new JFrame(), titleString, icon);
            if (!dialog.showDialog(nameString, seed)) {
                return;
            }
            if (dialog.allowOverwrite()) {
                allowOverwrite = true;
            }
            seed = dialog.getSeed();
            threadCount = dialog.getThreadPoolSize();
            useBeagle = dialog.useBeagle();
            if (useBeagle) {
                beagleShowInfo = dialog.showBeagleInfo();
                if (dialog.preferBeagleCPU()) {
                    beagleFlags |= BeagleFlag.PROCESSOR_CPU.getMask();
                }
                if (dialog.preferBeagleSSE()) {
                    beagleFlags |= BeagleFlag.VECTOR_SSE.getMask();
                }
                if (dialog.preferBeagleGPU()) {
                    beagleFlags |= BeagleFlag.PROCESSOR_GPU.getMask();
                }
                if (dialog.preferBeagleDouble()) {
                    beagleFlags |= BeagleFlag.PRECISION_DOUBLE.getMask();
                }
                if (dialog.preferBeagleSingle()) {
                    beagleFlags |= BeagleFlag.PRECISION_SINGLE.getMask();
                }
                System.setProperty("beagle.scaling", dialog.scalingScheme());
            }
            inputFile = dialog.getInputFile();
            if (!beagleShowInfo && inputFile == null) {
                System.err.println("No input file specified");
                return;
            }
        }
        if (beagleShowInfo) {
            BeagleInfo.printResourceList();
            return;
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
                inputFile = Utils.getLoadFile("BEAST " + version.getVersionString() + " - Select XML input file");
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
        if (useJava) {
            System.setProperty("java.only", "true");
        }
        if (fileNamePrefix != null && fileNamePrefix.trim().length() > 0) {
            System.setProperty("file.name.prefix", fileNamePrefix.trim());
        }
        if (allowOverwrite) {
            System.setProperty("log.allow.overwrite", "true");
        }
        if (useBeagle) {
            additionalParsers.add("beagle");
        }
        if (beagleFlags != 0) {
            System.setProperty("beagle.preferred.flags", Long.toString(beagleFlags));
        }
        if (threadCount >= 0) {
            System.setProperty("thread.count", String.valueOf(threadCount));
        }
        MathUtils.setSeed(seed);
        System.out.println();
        System.out.println("Random number seed: " + seed);
        System.out.println();
        try {
            new BeastMain(inputFile, consoleApp, maxErrorCount, verbose, parserWarning, strictXML, additionalParsers);
        } catch (RuntimeException rte) {
            if (window) {
                System.out.println();
                System.out.println("BEAST has terminated with an error. Please select QUIT from the menu.");
            } else {
                System.exit(1);
            }
        }
        if (!window) {
            System.exit(0);
        }
    }
