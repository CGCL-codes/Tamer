    public long process(Reader reader, long lastModifiedDate, PrintWriter writer) throws IOException {
        SSIMediator ssiMediator = new SSIMediator(ssiExternalResolver, lastModifiedDate, debug);
        StringWriter stringWriter = new StringWriter();
        IOTools.flow(reader, stringWriter);
        String fileContents = stringWriter.toString();
        stringWriter = null;
        int index = 0;
        boolean inside = false;
        StringBuffer command = new StringBuffer();
        try {
            while (index < fileContents.length()) {
                char c = fileContents.charAt(index);
                if (!inside) {
                    if (c == COMMAND_START.charAt(0) && charCmp(fileContents, index, COMMAND_START)) {
                        inside = true;
                        index += COMMAND_START.length();
                        command.setLength(0);
                    } else {
                        if (!ssiMediator.getConditionalState().processConditionalCommandsOnly) {
                            writer.write(c);
                        }
                        index++;
                    }
                } else {
                    if (c == COMMAND_END.charAt(0) && charCmp(fileContents, index, COMMAND_END)) {
                        inside = false;
                        index += COMMAND_END.length();
                        String strCmd = parseCmd(command);
                        if (debug > 0) {
                            ssiExternalResolver.log("SSIProcessor.process -- processing command: " + strCmd, null);
                        }
                        String[] paramNames = parseParamNames(command, strCmd.length());
                        String[] paramValues = parseParamValues(command, strCmd.length(), paramNames.length);
                        String configErrMsg = ssiMediator.getConfigErrMsg();
                        SSICommand ssiCommand = (SSICommand) commands.get(strCmd.toLowerCase());
                        String errorMessage = null;
                        if (ssiCommand == null) {
                            errorMessage = "Unknown command: " + strCmd;
                        } else if (paramValues == null) {
                            errorMessage = "Error parsing directive parameters.";
                        } else if (paramNames.length != paramValues.length) {
                            errorMessage = "Parameter names count does not match parameter values count on command: " + strCmd;
                        } else {
                            if (!ssiMediator.getConditionalState().processConditionalCommandsOnly || ssiCommand instanceof SSIConditional) {
                                long lmd = ssiCommand.process(ssiMediator, strCmd, paramNames, paramValues, writer);
                                if (lmd > lastModifiedDate) {
                                    lastModifiedDate = lmd;
                                }
                            }
                        }
                        if (errorMessage != null) {
                            ssiExternalResolver.log(errorMessage, null);
                            writer.write(configErrMsg);
                        }
                    } else {
                        command.append(c);
                        index++;
                    }
                }
            }
        } catch (SSIStopProcessingException e) {
        }
        return lastModifiedDate;
    }
