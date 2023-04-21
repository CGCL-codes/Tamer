    public static void main(String[] args) throws Exception {
        Preferences.setAppName("OBO2Database");
        IOUtil.setUpLogging();
        logger.info("Starting OBO2Database");
        logger.info("OBO-Edit version = " + Preferences.getVersion());
        if (args.length == 0) printUsage(1);
        OBOFileAdapter.OBOAdapterConfiguration readConfig = new OBOFileAdapter.OBOAdapterConfiguration();
        readConfig.setBasicSave(false);
        readConfig.setFollowImports(false);
        OBDSQLDatabaseAdapterConfiguration writeConfig = new OBDSQLDatabaseAdapter.OBDSQLDatabaseAdapterConfiguration();
        writeConfig.setBasicSave(false);
        boolean parseObsoleteComments = false;
        boolean writeObsoleteComments = false;
        boolean fixDbxrefs = false;
        LinkedList<ScriptWrapper> scripts = new LinkedList<ScriptWrapper>();
        String formatVersion = "OBO_1_2";
        for (int i = 0; i < args.length; i++) logger.info("args[" + i + "] = |" + args[i] + "|");
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-formatversion")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                formatVersion = args[i];
                if (!(formatVersion.equals("OBO_1_2") || formatVersion.equals("OBO_1_0"))) printUsage(1);
            } else if (args[i].equals("-allowdangling")) {
                readConfig.setAllowDangling(true);
            } else if (args[i].equals("-followimports")) {
                readConfig.setFollowImports(true);
            } else if (args[i].equals("-runscript")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                String scriptFile = args[i];
                String script = IOUtil.readFile(scriptFile);
                ScriptWrapper wrapper = new ScriptWrapper();
                wrapper.setScript(script);
                for (i = i + 1; i < args.length; i++) {
                    if (args[i].equals(";")) {
                        break;
                    }
                    wrapper.getArgs().add(args[i]);
                }
                scripts.add(wrapper);
            } else if (args[i].equals("-o")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                OBOSerializationEngine.FilteredPath path = new OBOSerializationEngine.FilteredPath();
                path.setUseSessionReasoner(false);
                for (; i < args.length; i++) {
                    if (args[i].equals("-f")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        String filterFile = args[i];
                        Filter filter = FilterUtil.loadFilter(filterFile);
                        path.setDoFilter(filter != null);
                        path.setObjectFilter(filter);
                    } else if (args[i].equals("-allowdangling")) {
                        path.setAllowDangling(true);
                    } else if (args[i].equals("-strictrootdetection")) {
                        path.setRootAlgorithm("STRICT");
                    } else if (args[i].equals("-saveimpliedlinks")) {
                        path.setSaveImplied(true);
                        path.setImpliedType(OBOSerializationEngine.SAVE_TRIMMED_LINKS);
                    } else if (args[i].equals("-saveallimpliedlinks")) {
                        path.setSaveImplied(true);
                        path.setImpliedType(OBOSerializationEngine.SAVE_ALL);
                    } else if (args[i].equals("-realizeimpliedlinks")) {
                        path.setAssertImpliedLinks(true);
                    } else if (args[i].equals("-p")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        String prefilterProperty = args[i];
                        path.setPrefilterProperty(prefilterProperty);
                    } else {
                        path.setPath(args[i]);
                        break;
                    }
                }
                System.err.println("Allowdangling = " + path.getAllowDangling());
                if (path.getPath() == null) printUsage(1); else writeConfig.getSaveRecords().add(path);
            } else if (args[i].equals("-?")) {
                printUsage(0);
            } else if (args[i].equals("-u")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                writeConfig.setDbUsername(args[i]);
            } else if (args[i].equals("-p")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                writeConfig.setDbPassword(args[i]);
            } else {
                readConfig.getReadPaths().add(args[i]);
            }
        }
        if (!Arrays.asList(args).contains("-u")) {
            String pguser = System.getenv("PGUSER");
            if (pguser != null) {
                writeConfig.setDbUsername(pguser);
            }
        }
        if (!Arrays.asList(args).contains("-p")) {
            String pgpassword = System.getenv("PGPASSWORD");
            if (pgpassword != null) {
                writeConfig.setDbPassword(pgpassword);
            }
        }
        if (readConfig.getReadPaths().size() < 1) {
            logger.info("You must specify at least one file to load.");
            printUsage(1);
        }
        if (writeConfig.getSaveRecords().size() < 1) {
            if (scripts.size() == 0) {
                System.err.println("You must specify at least one file to save.");
                printUsage(1);
            }
        }
        writeConfig.setSerializer(formatVersion);
        convertFiles(readConfig, writeConfig, parseObsoleteComments, writeObsoleteComments, fixDbxrefs, scripts);
    }
