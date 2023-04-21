    public File extractXCDL(DigitalObject input, URI inputFormat, File xcelFile, List<Parameter> parameters) {
        if (EXTRACTOR_HOME == null) {
            System.err.println("EXTRACTOR_HOME is not set! Please create an system variable\n" + "and point it to the Extractor installation folder!");
            plogger.error("EXTRACTOR_HOME is not set! Please create an system variable\n" + "and point it to the Extractor installation folder!");
        }
        plogger.info("Starting " + thisExtractorName + " Service...");
        List<String> extractor_arguments = null;
        File extractor_work_folder = null;
        File extractor_in_folder = null;
        File extractor_out_folder = null;
        extractor_work_folder = FileUtils.createFolderInWorkFolder(FileUtils.getPlanetsTmpStoreFolder(), extractorWork);
        extractor_in_folder = FileUtils.createFolderInWorkFolder(extractor_work_folder, EXTRACTOR_IN);
        extractor_out_folder = FileUtils.createFolderInWorkFolder(extractor_work_folder, EXTRACTOR_OUT);
        String inputFileName = DigitalObjectUtils.getFileNameFromDigObject(input, inputFormat);
        if (inputFileName == null || inputFileName.equalsIgnoreCase("")) {
            inputFileName = FileUtils.randomizeFileName(defaultInputFileName);
        } else {
            inputFileName = FileUtils.randomizeFileName(inputFileName);
        }
        outputFileName = getOutputFileName(inputFileName, format.createExtensionUri("xcdl"));
        File srcFile = new File(extractor_in_folder, inputFileName);
        FileUtils.writeInputStreamToFile(input.getContent().read(), srcFile);
        ProcessRunner shell = new ProcessRunner();
        plogger.info("EXTRACTOR_HOME = " + EXTRACTOR_HOME);
        plogger.info("Configuring Commandline");
        extractor_arguments = new ArrayList<String>();
        extractor_arguments.add(EXTRACTOR_HOME + EXTRACTOR_TOOL);
        String srcFilePath = srcFile.getAbsolutePath().replace('\\', '/');
        plogger.info("Input-Image file path: " + srcFilePath);
        extractor_arguments.add(srcFilePath);
        String outputFilePath = extractor_out_folder.getAbsolutePath() + File.separator + outputFileName;
        outputFilePath = outputFilePath.replace('\\', '/');
        if (xcelFile != null) {
            String xcelFilePath = xcelFile.getAbsolutePath().replace('\\', '/');
            plogger.info("Input-XCEL file path: " + xcelFilePath);
            extractor_arguments.add(xcelFilePath);
            extractor_arguments.add(outputFilePath);
        } else {
            extractor_arguments.add("-o");
            extractor_arguments.add(outputFilePath);
        }
        if (parameters != null) {
            if (parameters.size() != 0) {
                plogger.info("Got additional parameters: ");
                for (Iterator<Parameter> iterator = parameters.iterator(); iterator.hasNext(); ) {
                    Parameter parameter = (Parameter) iterator.next();
                    String name = parameter.getName();
                    if (name.equalsIgnoreCase(OPTIONAL_XCEL_PARAM)) {
                        plogger.info("Optional XCEL passed! Using specified XCEL.");
                        continue;
                    }
                    if (name.equalsIgnoreCase(RAW_DATA_FLAG)) {
                        plogger.info("Got Parameter: " + name + " = " + parameter.getValue());
                        plogger.info("Configuring Extractor to write RAW data!");
                        extractor_arguments.add(parameter.getValue());
                        continue;
                    } else if (name.equalsIgnoreCase(NO_NORM_DATA_FLAG)) {
                        plogger.info("Got Parameter: " + name + " = " + parameter.getValue());
                        plogger.info("Configuring Extractor to skip NormData!");
                        extractor_arguments.add(parameter.getValue());
                        continue;
                    } else {
                        plogger.warn("Invalid parameter: " + name + " = '" + parameter.getValue() + "'. Ignoring parameter...!");
                        continue;
                    }
                }
            }
        }
        String line = "";
        for (String argument : extractor_arguments) {
            line = line + argument + " ";
        }
        plogger.info("Setting command to: " + line);
        shell.setCommand(extractor_arguments);
        shell.setStartingDir(new File(EXTRACTOR_HOME));
        plogger.info("Setting starting Dir to: " + EXTRACTOR_HOME);
        plogger.info("Starting Extractor tool...");
        shell.run();
        String processOutput = shell.getProcessOutputAsString();
        String processError = shell.getProcessErrorAsString();
        plogger.info("Process Output: " + processOutput);
        System.out.println("Process Output: " + processOutput);
        if (!"".equals(processError)) {
            plogger.error("Process Error: " + processError);
            System.err.println("Process Error: " + processError);
        }
        plogger.info("Creating File to return...");
        File resultXCDL = new File(outputFilePath);
        if (!resultXCDL.exists()) {
            plogger.error("File doesn't exist: " + resultXCDL.getAbsolutePath());
            return null;
        }
        return resultXCDL;
    }
