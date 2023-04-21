    public static void main(String... args) {
        String parameter, pathStr;
        int threads = 4;
        File dir;
        boolean noVerify = false;
        Hashtable<String, Integer> typeTable = new Hashtable<String, Integer>();
        baseItemRule = new Hashtable<Integer, Integer>();
        TDAFile baseItems2da = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) != '-') {
                System.err.println("The " + i + "'th option was not a switch, which was expected!");
                return;
            }
            switch(args[i].charAt(2)) {
                case 'b':
                case 'B':
                    if (args[i].equalsIgnoreCase("--benchmark")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "HDD-read, GFF-read, GFF-write\nTesting harddisk-read, gff-read and gff-read+write speeds respectively.");
                            return;
                        }
                        parameter = parameter.trim().toLowerCase();
                        if (parameter.equals("hdd-read")) {
                            benchmarkJob = BENCHMARK_TEST_HDD_READ;
                        } else if (parameter.equals("gff-read")) {
                            benchmarkJob = BENCHMARK_TEST_GFF_READ;
                        } else if (parameter.equals("gff-write")) {
                            benchmarkJob = BENCHMARK_TEST_GFF_WRITE;
                        } else {
                            invalidParameter(parameter, args[i], "HDD-read, GFF-read, GFF-write\nTesting harddisk-read, gff-read and gff-read+write speeds respectively.");
                            return;
                        }
                        i++;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'i':
                case 'I':
                    if (args[i].equalsIgnoreCase("--ignore-missing-resrefs")) {
                        rules |= RULE_IGNORE_ITEMS_MISSING_RESREFS;
                        break;
                    } else if (args[i].equalsIgnoreCase("--ignore-empty-resrefs")) {
                        rules |= RULE_IGNORE_ITEMS_EMPTY_RESREFS;
                        break;
                    } else if (args[i].equalsIgnoreCase("--ignore-unknown-resrefs")) {
                        rules |= RULE_IGNORE_ITEMS_UNKNOWN_RESREFS;
                        break;
                    } else if (args[i].equalsIgnoreCase("--ignore-type")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "Requires the label or the \"rowindex\" of the item type to ignore (from the baseitems.2da file)");
                            return;
                        }
                        parameter = parameter.trim().toLowerCase();
                        char first = parameter.charAt(0);
                        if ('0' <= first && first <= '9') {
                            baseItemRule.put(Integer.parseInt(parameter), SPECIAL_RULE_IGNORE);
                        } else {
                            typeTable.put(parameter, SPECIAL_RULE_IGNORE);
                        }
                        i++;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'l':
                case 'L':
                    if (args[i].equalsIgnoreCase("--limit")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "A positive integer defining the upper limits of log-ins to read.");
                            return;
                        }
                        i++;
                        try {
                            updateDirLimit = Integer.parseInt(parameter);
                            if (updateDirLimit < 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("You call \"" + parameter + "\" positive integer?");
                            return;
                        }
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'n':
                case 'N':
                    if (args[i].equalsIgnoreCase("--no-verify")) {
                        noVerify = true;
                        break;
                    } else if (args[i].equalsIgnoreCase("--nwn-dir")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "The location of your NWN folder (it contains chitin.key).");
                            return;
                        }
                        i++;
                        dir = new File(parameter);
                        if (!dir.exists() || !dir.isDirectory()) {
                            try {
                                pathStr = dir.getCanonicalPath();
                            } catch (IOException e) {
                                pathStr = dir.getAbsolutePath();
                            }
                            System.err.println("Could not find a dir denoted by the path: " + pathStr);
                            return;
                        }
                        if (!new File(dir.getPath() + File.separatorChar + "chitin.key").exists()) {
                            invalidParameter(null, args[i], dir.getPath() + " does not contain the \"chitin.key\" file.");
                            return;
                        }
                        nwnDir = dir;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'o':
                case 'O':
                    if (args[i].equalsIgnoreCase("--output-dir")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "The dir the updated characters should be placed in.");
                            return;
                        }
                        i++;
                        dir = new File(parameter);
                        if (dir.exists() && !dir.isDirectory()) {
                            try {
                                pathStr = dir.getCanonicalPath();
                            } catch (IOException e) {
                                pathStr = dir.getAbsolutePath();
                            }
                            System.err.println(pathStr + " is not a dir.");
                            return;
                        }
                        outputDir = dir;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'r':
                case 'R':
                    if (args[i].equalsIgnoreCase("--remove-type")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "Requires the label or the \"rowindex\" of the item type to ignore (from the baseitems.2da file)");
                            return;
                        }
                        parameter = parameter.trim().toLowerCase();
                        char first = parameter.charAt(0);
                        if ('0' <= first && first <= '9') {
                            baseItemRule.put(Integer.parseInt(parameter), SPECIAL_RULE_DELETE);
                        } else {
                            typeTable.put(parameter, SPECIAL_RULE_DELETE);
                        }
                        i++;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 's':
                case 'S':
                    if (args[i].equalsIgnoreCase("--silent")) {
                        verbose = false;
                        break;
                    } else if (args[i].equalsIgnoreCase("--simulate")) {
                        benchmarkJob = BENCHMARK_TEST_GFF_READ;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 't':
                case 'T':
                    if (args[i].equalsIgnoreCase("--threads")) {
                        parameter = getSwitchParameter(i, args);
                        char first;
                        if (parameter == null) {
                            invalidParameter(null, args[i], "An integer between 1 and 9 inclusive.");
                            return;
                        }
                        i++;
                        first = parameter.charAt(0);
                        if (parameter.length() > 1 || first < '0' || first > '9') {
                            invalidParameter(parameter, args[i], "An integer between 1 and 9 inclusive.");
                            return;
                        }
                        threads = first - '0';
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'u':
                case 'U':
                    if (args[i].equalsIgnoreCase("--update-dir")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "The dir from where the updated uti-files are located.");
                            return;
                        }
                        i++;
                        dir = new File(parameter);
                        if (!dir.exists() || !dir.isDirectory()) {
                            try {
                                pathStr = dir.getCanonicalPath();
                            } catch (IOException e) {
                                pathStr = dir.getAbsolutePath();
                            }
                            System.err.println("Could not a dir denoted by the path: " + pathStr);
                            return;
                        }
                        utiDir = dir;
                        break;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                case 'v':
                case 'V':
                    if (args[i].equalsIgnoreCase("--vault")) {
                        parameter = getSwitchParameter(i, args);
                        if (parameter == null) {
                            invalidParameter(null, args[i], "The vault from where the characters should be found.");
                            return;
                        }
                        i++;
                        dir = new File(parameter);
                        if (!dir.exists() || !dir.isDirectory()) {
                            try {
                                pathStr = dir.getCanonicalPath();
                            } catch (IOException e) {
                                pathStr = dir.getAbsolutePath();
                            }
                            System.err.println("Could not find a dir denoted by the path: " + pathStr);
                            return;
                        }
                        vaultTopDir = dir;
                        break;
                    } else if (args[i].equalsIgnoreCase("--verbose")) {
                        verbose = true;
                        break;
                    } else if (args[i].equalsIgnoreCase("--version")) {
                        printVersionData();
                        return;
                    }
                    parseNotRecognizedSwitch(args[i]);
                    return;
                default:
                    parseNotRecognizedSwitch(args[i]);
                    return;
            }
        }
        if (vaultTopDir == null) {
            if (nwnDir == null) {
                System.out.println("No \"to-update\"-dir given. Use the \"--vault <Dir>\" or the \"--nwn-dir <Dir>\" switch");
                return;
            }
            vaultTopDir = new File(nwnDir.getPath() + File.separatorChar + "servervault");
        }
        if (!vaultTopDir.exists() || !vaultTopDir.isDirectory() || !vaultTopDir.canRead()) {
            System.err.println("Could not read the contents (the vault) of " + vaultTopDir.getAbsolutePath());
            System.err.println("Please verify this location exists, is a directory and is readable.");
            return;
        }
        try {
            resMap = SimpleResourceTypeMap.loadStandardMap();
            if (nwnDir != null) {
                resManager = ResourceManager.findResourceManager(nwnDir, resMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (typeTable.size() > 0) {
            if (nwnDir == null) {
                System.err.println("Cannot look up item base types without knowing where to find baseitems.2da. Please use the \"--nwn-dir <path>\" switch");
                return;
            }
            ResourceContainer resCon = null;
            try {
                Resource res = resManager.makeResource("baseitems.2da");
                resCon = resManager.whichResource(res);
                if (resCon == null) {
                    throw new IOException("Could not locate baseitems.2da.");
                }
                baseItems2da = TDAFile.read2DAFile(resCon, res);
                int rowIndex = 0;
                String label;
                Integer rule;
                int typesLeft = typeTable.size();
                while (typesLeft > 0 && rowIndex < baseItems2da.getRowCount()) {
                    label = baseItems2da.getValueAt(rowIndex, "label").getDataAsString();
                    rule = typeTable.get(label);
                    if (rule != null) {
                        typeTable.remove(label);
                        if (baseItemRule.containsKey(rowIndex)) {
                            System.err.println("Two rules given for \"" + label + "\" (" + rowIndex + "), please restart with only one rule.");
                            return;
                        }
                        baseItemRule.put(rowIndex, rule);
                        typesLeft--;
                    }
                    rowIndex++;
                }
                if (typeTable.size() > 0) {
                    for (Map.Entry<String, Integer> entry : typeTable.entrySet()) {
                        System.err.println("Could not find type for label: " + entry.getKey());
                    }
                    return;
                }
            } catch (IOException e) {
                System.err.println("Could not look up the baseitem-labels from the baseitems.2da");
                e.printStackTrace();
            } finally {
                if (resCon != null) {
                    try {
                        resCon.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        typeTable = null;
        if (utiDir == null) {
            System.err.println("No \"replace-with\"-dir given. Use the \"--update-dir <Dir>\" switch");
            return;
        }
        if (outputDir == null && benchmarkJob == BENCHMARK_TEST_GFF_WRITE) {
            System.err.println("No \"output\"-dir given on a write-test. Use the \"--output-dir <Dir>\" switch");
            return;
        }
        vaultDirs = vaultTopDir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        File[] utiFiles = utiDir.listFiles(Filters.createFilenameFilter(new RegexNameFilter("\\.uti$", false)));
        utiHash = new Hashtable<String, File>(utiFiles.length);
        for (int i = 0; i < utiFiles.length; i++) {
            utiHash.put(utiFiles[i].getName().toLowerCase().replaceAll("^(.*" + File.separatorChar + ")?(.*)\\..{3}$", "$2"), utiFiles[i]);
            System.out.println("Found: " + utiFiles[i].getName().toLowerCase().replaceAll("^(.*" + File.separatorChar + ")?(.*)\\..{3}$", "$2"));
        }
        if (!noVerify) {
            String job = "[N/A]";
            char rule;
            printVersionData();
            System.out.println("Planned run using " + threads + " asynchronious updater" + (threads > 1 ? 's' : "") + '!');
            System.out.println("Vault/Reading from: " + vaultTopDir + " ( " + vaultDirs.length + " logins.)");
            System.out.println("Replacements from: " + utiDir + ", " + utiFiles.length + " items to sync out.");
            System.out.println("OutputDir is: " + (outputDir != null ? outputDir + (!outputDir.exists() ? "[C]" : "") : "[Not Specified]"));
            System.out.println("Log-in limit: " + (updateDirLimit > 0 ? updateDirLimit : "[None - updating " + vaultDirs.length + "]"));
            System.out.println("Verbosity: " + (verbose ? "Will print whenever a login was done." : "silent."));
            switch(benchmarkJob) {
                case BENCHMARK_TEST_GFF_READ:
                    job = "GFF-read (and update) speed test (no-writing).";
                    break;
                case BENCHMARK_TEST_GFF_WRITE:
                    job = "Complete update.";
                    break;
                case BENCHMARK_TEST_HDD_READ:
                    job = "HDD-read speed test.";
                    break;
            }
            System.out.println("Testing: " + job);
            System.out.println("Items with unknown resrefs: " + ((rules & RULE_IGNORE_ITEMS_UNKNOWN_RESREFS) == 0 ? "will be deleted!" : "will be ignored."));
            System.out.println("Items with empty resrefs: " + ((rules & RULE_IGNORE_ITEMS_EMPTY_RESREFS) == 0 ? "will be deleted!" : "will be ignored."));
            System.out.println("Items with without resrefs: " + ((rules & RULE_IGNORE_ITEMS_MISSING_RESREFS) == 0 ? "will be deleted!" : "will be ignored."));
            System.out.println("Note: Containers (e.g. Magical bags) will always be ignored if they contain items.");
            System.out.println();
            System.out.print("Type rules:");
            for (Map.Entry<Integer, Integer> entry : baseItemRule.entrySet()) {
                if (baseItems2da != null) {
                    System.out.print(" " + baseItems2da.getValueAt(entry.getKey(), "label") + '(' + entry.getKey() + ')');
                } else {
                    System.out.print(" " + entry.getKey());
                }
                switch(entry.getValue()) {
                    case SPECIAL_RULE_DELETE:
                        rule = 'D';
                        break;
                    default:
                    case SPECIAL_RULE_IGNORE:
                        rule = 'I';
                        break;
                }
                System.out.print("[" + rule + ']');
            }
            System.out.println();
            System.out.println();
            System.out.println("You can skip this by adding a \"--no-verify\".");
            System.out.println("Answer \"y\" to continue, anything else (which does not start with \"y\") will abort");
            try {
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                String reply = buff.readLine();
                if (reply == null || reply.trim().equals("")) {
                    System.err.println("Abort!");
                    return;
                }
                if (reply.trim().toLowerCase().charAt(0) != 'y') {
                    System.err.println("Abort!");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Abort!");
                return;
            }
        }
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            try {
                pathStr = outputDir.getCanonicalPath();
            } catch (IOException e) {
                pathStr = outputDir.getAbsolutePath();
            }
            System.err.println("Could not create a dir at: " + pathStr);
            return;
        }
        replacers = new GFFDataReplacer[threads];
        for (int i = 0; i < threads; i++) {
            replacers[i] = new GFFDataReplacer();
        }
        if (updateDirLimit == 0) {
            updateDirLimit = vaultDirs.length;
        } else {
            updateDirLimit = Math.min(vaultDirs.length, updateDirLimit);
        }
        startTime = System.currentTimeMillis();
        for (int i = 0; i < replacers.length; i++) {
            replacers[i].start();
        }
    }
