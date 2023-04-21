    public static void main(String[] argv) {
        ASFormatter formatter = new ASFormatter();
        Vector fileNameVector = new Vector();
        Vector optionsVector = new Vector();
        String optionsFileName = "";
        boolean ok = true;
        boolean shouldPrintHelp = false;
        boolean shouldParseOptionsFile = true;
        _err = System.err;
        _suffix = ".orig";
        _modeManuallySet = false;
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (IS_PARAM_OPTION(arg, "--options=none")) {
                shouldParseOptionsFile = false;
            } else if (IS_PARAM_OPTION(arg, "--options=")) {
                optionsFileName = GET_PARAM(arg, "--options=");
            } else if (IS_OPTION(arg, "-h") || IS_OPTION(arg, "--help") || IS_OPTION(arg, "-?")) {
                shouldPrintHelp = true;
            } else if (arg.charAt(0) == '-') {
                optionsVector.addElement(arg);
            } else {
                fileNameVector.addElement(arg);
            }
        }
        if (shouldParseOptionsFile) {
            if (optionsFileName.length() == 0) {
                String env = System.getProperty("ASTYLE_OPTIONS");
                if (env != null) {
                    optionsFileName = env;
                }
            }
            if (optionsFileName.length() == 0) {
                String env = System.getProperty("user.home");
                if (env != null) {
                    optionsFileName = env + "/.astylerc";
                    File optFile = new File(optionsFileName);
                    if (!optFile.exists() || !optFile.canRead()) {
                        optionsFileName = "";
                        optFile = null;
                    }
                }
            }
            if (optionsFileName.length() != 0) try {
                BufferedReader optionsIn = new BufferedReader(new FileReader(optionsFileName));
                Vector fileOptionsVector = new Vector();
                try {
                    importOptions(optionsIn, fileOptionsVector);
                    ok = parseOptions(formatter, fileOptionsVector, "Unknown option in default options file: ");
                    optionsIn.close();
                } catch (IOException ioex) {
                    _err.println("Error reading options file: " + optionsFileName);
                    ok = false;
                }
                if (!ok) {
                    _err.println("For help on options, type 'astyle -h'.");
                }
            } catch (FileNotFoundException fnfex) {
                error("Could not open astyle options file:", optionsFileName);
            }
        }
        ok = parseOptions(formatter, optionsVector, "Unknown command line option: ");
        if (!ok) {
            _err.println("For help on options, type 'astyle -h'.");
            System.exit(1);
        }
        if (shouldPrintHelp) {
            printHelp();
            System.exit(1);
        }
        if (fileNameVector.isEmpty()) {
            formatter.init(new ASStreamIterator(System.in));
            while (formatter.hasMoreLines()) {
                System.out.print(formatter.nextLine());
                if (formatter.hasMoreLines()) {
                    System.out.println();
                }
            }
            System.out.flush();
        } else {
            for (int i = 0; i < fileNameVector.size(); i++) {
                String origFileName = (String) fileNameVector.elementAt(i);
                String inFileName = origFileName + _suffix;
                File origFile = new File(origFileName);
                File inFile = new File(inFileName);
                if (inFile.exists() && !inFile.delete()) {
                    error("Could not delete file", inFile.toString());
                }
                if (!origFile.renameTo(inFile)) {
                    error("Could not rename", origFile.toString() + " to " + inFile.toString());
                }
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader(inFile));
                } catch (FileNotFoundException fnfex) {
                    error("Could not open input file", inFile.toString());
                }
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(origFile));
                } catch (IOException ioex) {
                    error("Could not open output file", origFile.toString());
                }
                if (!_modeManuallySet) {
                    if (origFileName.endsWith(".java")) {
                        formatter.setCStyle(false);
                    } else {
                        formatter.setCStyle(true);
                    }
                }
                formatter.init(new ASStreamIterator(in));
                try {
                    while (formatter.hasMoreLines()) {
                        String line = formatter.nextLine();
                        out.write(line, 0, line.length());
                        if (formatter.hasMoreLines()) {
                            out.newLine();
                        }
                    }
                    out.flush();
                } catch (IOException ioex) {
                    error("Could not write to output file", origFile.toString());
                }
                try {
                    out.close();
                } catch (IOException ioex) {
                }
                try {
                    in.close();
                } catch (IOException ioex) {
                }
            }
        }
        return;
    }
