    public String process(String input) throws JobExecutionException {
        try {
            File inputDataFile = File.createTempFile("instantsoap-r", ".inp");
            File outputDataFile = File.createTempFile("instantsoap-r", ".out");
            File rScriptFile = File.createTempFile("instantsoap-r", ".r");
            writeStringToFile(inputDataFile, input);
            writeStringToFile(rScriptFile, readStringFromInputStream(getInputStreamForRScript(getClass(), getRScriptName())));
            Map<String, String> commandline = new HashMap<String, String>();
            commandline.put("input", inputDataFile.getCanonicalPath());
            commandline.put("output", outputDataFile.getCanonicalPath());
            commandline.put("resource", rScriptFile.getCanonicalPath());
            String debug = executeR(commandline);
            System.out.println(debug);
            String retn = readStringFromFile(outputDataFile).trim();
            return retn;
        } catch (IOException exp) {
            throw new JobExecutionException("There has been an error in the execution of R script: " + getRScriptName(), exp);
        }
    }
