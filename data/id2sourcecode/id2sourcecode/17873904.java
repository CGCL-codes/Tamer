    public static void main(String[] args) throws Exception {
        IOUtil.setUpLogging();
        logger.info("version = " + Preferences.getVersion());
        if (args.length == 0) printUsage(1);
        OBOFileAdapter.OBOAdapterConfiguration readConfig = new OBOFileAdapter.OBOAdapterConfiguration();
        readConfig.setBasicSave(false);
        OBOFileAdapter.OBOAdapterConfiguration writeConfig = new OBOFileAdapter.OBOAdapterConfiguration();
        writeConfig.setBasicSave(false);
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
            } else if (args[i].equals("-includeexplanations")) {
                writeConfig.setIncludeExplanations(true);
            } else if (args[i].equals("-includenames")) {
                writeConfig.setIncludeNames(true);
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
                path.setImpliedType(OBOSerializationEngine.SAVE_ALL);
                for (; i < args.length; i++) {
                    if (args[i].equals("-f")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        String filterFile = args[i];
                        Filter filter = FilterUtil.loadFilter(filterFile);
                        path.setDoFilter(filter != null);
                        path.setObjectFilter(filter);
                    } else if (args[i].equals("-reasonerfactory")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        path.setReasonerFactory((ReasonerFactory) Class.forName(args[i]).newInstance());
                    } else if (args[i].equals("-allowdangling")) {
                        path.setAllowDangling(true);
                    } else if (args[i].equals("-strictrootdetection")) {
                        path.setRootAlgorithm("STRICT");
                    } else if (args[i].equals("-filterredundant")) {
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
            } else {
                readConfig.getReadPaths().add(args[i]);
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
        convertFiles(readConfig, writeConfig, scripts);
    }
