    public String execute(String cmd, boolean ignoreErrors) {
        if (!isRunning()) throw new RProcessorDeadException("R process has been closed.");
        Matcher m = singleCmdPatt.matcher(cmd);
        if (!m.matches()) throw new RProcessorException("execute() may only be given one command at a time");
        StringBuilder sentinelCmd = new StringBuilder(cmd.trim());
        sentinelCmd.append('\n');
        if (recordMode == RecordMode.CMDS_ONLY || recordMode == RecordMode.FULL) interactionRecord.append(sentinelCmd);
        if (debugOutputMode == RecordMode.CMDS_ONLY || debugOutputMode == RecordMode.FULL) System.out.print("> " + sentinelCmd);
        try {
            String results = null;
            boolean errorOccurred = false;
            synchronized (processSync) {
                sentinelCmd.append(this.SENTINEL_STRING_CMD);
                byte[] cmdArray = sentinelCmd.toString().getBytes();
                procIn.write(cmdArray, 0, cmdArray.length);
                procIn.flush();
                StringBuilder sb = new StringBuilder();
                String line = procOut.readLine();
                while (line != null && !line.equals(this.SENTINEL_STRING_RETURN)) {
                    sb.append(line);
                    sb.append('\n');
                    if (!ignoreErrors && (line.startsWith("Error") || line.startsWith("Warning"))) errorOccurred = true;
                    line = procOut.readLine();
                }
                results = sb.toString();
            }
            if (recordMode == RecordMode.OUTPUT_ONLY || recordMode == RecordMode.FULL) interactionRecord.append(results);
            if (debugOutputMode == RecordMode.OUTPUT_ONLY || debugOutputMode == RecordMode.FULL) System.out.print(results);
            if (errorOccurred) throw new RProcessorException("R: " + results);
            return results;
        } catch (IOException ex) {
            close();
            throw new RProcessorException("Unable to read or write to the R instance", ex);
        }
    }
