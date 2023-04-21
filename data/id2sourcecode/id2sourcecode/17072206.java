    public void insertProbes(int programID, Map<String, List<String>> moduleIncludeMap) throws RemoteException {
        log.debug("insertProbes called");
        try {
            Program program = programManager.getProgram(programID);
            File makefile = new File(program.getSourcePath(), "Makefile");
            String topLevelConfiguration = programWeaverManager.findComponentFromMakefile(makefile);
            String topLevelPath = new File(program.getSourcePath(), topLevelConfiguration + ".nc").getAbsolutePath();
            backup(program.getSourcePath());
            log.info("Top-level config: " + topLevelConfiguration);
            List<String> prependList = new ArrayList<String>(1);
            List<String> appendList = new ArrayList<String>(3);
            List<String> gccArgs = new ArrayList<String>(2);
            prependList.add(NESTBED_NESC_ROOT + "/ModifiedTinyOS");
            appendList.add(NESTBED_NESC_ROOT + "/TraceRecorder");
            appendList.add(NESTBED_NESC_ROOT + "/MemoryProfiler");
            appendList.add(NESTBED_NESC_ROOT + "/NestbedControl");
            gccArgs.add("-I");
            gccArgs.add(NESTBED_NESC_ROOT + "/TraceRecorder");
            gccArgs.add("-I");
            gccArgs.add(NESTBED_NESC_ROOT + "/ModifiedTinyOS");
            gccArgs.add("-I");
            gccArgs.add(NESTBED_NESC_ROOT + "/MemoryProfiler");
            gccArgs.add("-I");
            gccArgs.add(NESTBED_NESC_ROOT + "/NestbedControl");
            log.debug("Prepend list: " + prependList);
            log.debug("Append list:  " + appendList);
            File topLevelFile = new File(topLevelPath).getAbsoluteFile();
            File analysisDir = topLevelFile.getParentFile();
            log.info("Top-Level file:    " + topLevelFile.getAbsolutePath());
            log.info("Basedir directory: " + analysisDir.getAbsolutePath());
            TraceInstrumentor traceInstrumentor;
            traceInstrumentor = new TraceInstrumentor(prependList, appendList, gccArgs, topLevelFile.getAbsolutePath(), analysisDir);
            traceInstrumentor.setModuleIncludeMap(moduleIncludeMap);
            traceInstrumentor.enableTrace();
            traceInstrumentor.handleCommit();
            File source = new File(NESTBED_NESC_ROOT + "/TraceRecorder/TraceRecorder.h");
            File dest = new File(program.getSourcePath() + "/analysis/TraceRecorder.h");
            FileUtils.copyFile(source, dest);
            log.info("Moving source files from analysis directory down...");
            moveFiles(program.getSourcePath());
            traceInstrumentor.generateRadioHeader(program.getSourcePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
