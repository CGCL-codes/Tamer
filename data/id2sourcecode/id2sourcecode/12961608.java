    private static DbLoad parseArgs(String argv[]) throws Exception {
        boolean noOverwrite = false;
        boolean textFileMode = false;
        boolean ignoreUnknownConfig = false;
        boolean showProgressInterval = false;
        int argc = 0;
        int nArgs = argv.length;
        String inputFileName = null;
        File envHome = null;
        String dbName = null;
        long progressInterval = 0;
        DbLoad ret = new DbLoad();
        ret.setCommandLine(true);
        while (argc < nArgs) {
            String thisArg = argv[argc++].trim();
            if (thisArg.equals("-n")) {
                noOverwrite = true;
            } else if (thisArg.equals("-T")) {
                textFileMode = true;
            } else if (thisArg.equals("-I")) {
                ignoreUnknownConfig = true;
            } else if (thisArg.equals("-V")) {
                System.out.println(JEVersion.CURRENT_VERSION);
                System.exit(0);
            } else if (thisArg.equals("-f")) {
                if (argc < nArgs) {
                    inputFileName = argv[argc++];
                } else {
                    printUsage("-f requires an argument");
                }
            } else if (thisArg.equals("-h")) {
                if (argc < nArgs) {
                    envHome = new File(argv[argc++]);
                } else {
                    printUsage("-h requires an argument");
                }
            } else if (thisArg.equals("-s")) {
                if (argc < nArgs) {
                    dbName = argv[argc++];
                } else {
                    printUsage("-s requires an argument");
                }
            } else if (thisArg.equals("-c")) {
                if (argc < nArgs) {
                    try {
                        ret.loadConfigLine(argv[argc++]);
                    } catch (IllegalArgumentException e) {
                        printUsage("-c: " + e.getMessage());
                    }
                } else {
                    printUsage("-c requires an argument");
                }
            } else if (thisArg.equals("-v")) {
                showProgressInterval = true;
            }
        }
        if (envHome == null) {
            printUsage("-h is a required argument");
        }
        long totalLoadBytes = 0;
        InputStream is;
        if (inputFileName == null) {
            is = System.in;
            if (showProgressInterval) {
                printUsage("-v requires -f");
            }
        } else {
            is = new FileInputStream(inputFileName);
            if (showProgressInterval) {
                totalLoadBytes = ((FileInputStream) is).getChannel().size();
                progressInterval = totalLoadBytes / 20;
            }
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        Environment env = new Environment(envHome, envConfig);
        ret.setEnv(env);
        ret.setDbName(dbName);
        ret.setInputReader(reader);
        ret.setNoOverwrite(noOverwrite);
        ret.setTextFileMode(textFileMode);
        ret.setIgnoreUnknownConfig(ignoreUnknownConfig);
        ret.setProgressInterval(progressInterval);
        ret.setTotalLoadBytes(totalLoadBytes);
        return ret;
    }
