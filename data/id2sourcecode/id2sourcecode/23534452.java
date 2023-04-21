    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
        if (call1 == null) {
            handleException(context, new ActionStepException("No current call found"));
            return;
        } else if (!(call1 instanceof Call)) {
            handleException(context, new ActionStepException("Call isn't isn't an Asterisk call: " + call1.getClass().getName()));
            return;
        }
        if (((Call) call1).getChannel() == null) {
            handleException(context, new ActionStepException("No channel found in current context"));
            return;
        }
        AgiChannel channel = ((Call) call1).getChannel();
        try {
            List<String> files = new ArrayList<String>();
            if (audioFilenames != null && !audioFilenames.isEmpty()) {
                for (CaseItem item : audioFilenames) {
                    Object dynValue = resolveDynamicValue(item.getDynamicValue(), context);
                    String filenameStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
                    if (StringUtils.isBlank(filenameStr)) continue;
                    if (item.getDynamicValue().getType() == DynamicValueType.CUSTOM) {
                        filenameStr = getSaflet().getPromptPathByName(filenameStr);
                    }
                    files.add(filenameStr);
                }
                if (files.isEmpty()) exception = new ActionStepException("No Filenames Specified"); else {
                    if (debugLog.isLoggable(Level.FINEST)) debug("Streaming files " + files);
                    StringBuffer appCmd = new StringBuffer();
                    for (int i = 0; i < files.size(); i++) {
                        if (i > 0) appCmd.append('&');
                        appCmd.append(files.get(i));
                    }
                    char c = (char) channel.exec("Background", appCmd.toString());
                    if (c > 0) {
                        ((AsteriskSafletContext) context).appendBufferedDigits(String.valueOf(c));
                    } else if (c == -1) {
                        exception = new ActionStepException("Channel was hung up.");
                    }
                    if (debugLog.isLoggable(Level.FINEST)) debug("StreamAudio returned " + translateAppReturnValue(c));
                }
            } else exception = new ActionStepException("No Filenames Specified");
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context);
    }
