    public RunResults run(ParameterMap parameters, int runNumber, long rngSeed, boolean dryRun) throws AdapterException {
        List<String> arguments = new ArrayList<String>();
        if (miscOptions != null) {
            arguments.addAll(StringUtils.tokenize(miscOptions));
        }
        arguments.add(runNumOption + runNumPrefix + runNumber);
        arguments.add(rngSeedOption + rngSeed);
        if (useInputFile && inputFilePath != null) {
            arguments.add(inputFileOption + inputFilePath);
        }
        for (String name : parameters.keySet()) {
            arguments.add(setParamOption + name + "=" + parameters.get(name));
        }
        StringBuilder messageBuilder = new StringBuilder(command);
        for (String arg : arguments) {
            messageBuilder.append(" " + StringUtils.escape(arg, " "));
        }
        int status = 0;
        String message = messageBuilder.toString();
        byte[] stdoutData = null;
        byte[] stderrData = null;
        if (!dryRun) {
            String[] cmdArray = new String[arguments.size() + 1];
            cmdArray[0] = command;
            for (int i = 0; i < arguments.size(); i++) {
                cmdArray[i + 1] = arguments.get(i);
            }
            try {
                Process process = Runtime.getRuntime().exec(cmdArray);
                OutputStream stdinStream = process.getOutputStream();
                if (stdinData != null) {
                    stdinStream.write(stdinData);
                }
                stdinStream.close();
                int b;
                InputStream stdoutStream = process.getInputStream();
                ByteArrayOutputStream stdoutByteStream = new ByteArrayOutputStream();
                while ((b = stdoutStream.read()) != -1) stdoutByteStream.write(b);
                stdoutData = stdoutByteStream.toByteArray();
                InputStream stderrStream = process.getErrorStream();
                ByteArrayOutputStream stderrByteStream = new ByteArrayOutputStream();
                while ((b = stderrStream.read()) != -1) stderrByteStream.write(b);
                stderrData = stderrByteStream.toByteArray();
            } catch (IOException e) {
                throw new AdapterException("Received IOException running process", e);
            }
        }
        return new RunResults(status, message, stdoutData, stderrData);
    }
