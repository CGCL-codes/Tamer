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
            String meetmeStr = null;
            if (conferenceNumber != null) {
                Object dynValue = resolveDynamicValue(conferenceNumber, context);
                meetmeStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            }
            if (StringUtils.isBlank(meetmeStr)) {
                exception = new ActionStepException("Conference number is required");
            } else {
                if (StringUtils.isNotBlank(recordingFilename)) {
                    channel.setVariable("MEETME_RECORDINGFILE", recordingFilename);
                }
                if (StringUtils.isNotBlank(recordingFormat)) {
                    channel.setVariable("RECORDINGFORMAT", recordingFormat);
                }
                if (StringUtils.isNotBlank(backgroundScriptAgi)) {
                    channel.setVariable("MEETME_AGI_BACKGROUND", backgroundScriptAgi);
                }
                StringBuffer args = new StringBuffer(meetmeStr);
                args.append('|');
                if (adminMode) args.append('a');
                if (allowPoundUserExit) args.append('p');
                if (!aloneMessageEnabled) args.append('1');
                if (alwaysPromptForPin) args.append('P');
                if (announceCount) args.append('c');
                if (announceJoinLeaveNoReview) args.append('I'); else if (announceJoinLeave) args.append('i');
                if (closeOnLastMarkedUserExit) args.append('x');
                String pinStr = null;
                if (pin != null) {
                    Object dynValue = resolveDynamicValue(pin, context);
                    pinStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
                }
                if (dynamicallyAddConference) args.append(StringUtils.isNotBlank(pinStr) ? 'D' : 'd');
                if (exitOnExtensionEntered) args.append('x');
                if (monitorOnlyMode) args.append('m');
                if (passDTMF) args.append('F');
                if (playMenuOnStar) args.append('s');
                if (quietMode) args.append('q');
                if (recordConference) args.append('r');
                if (selectEmptyPinlessConference) args.append('E'); else if (selectEmptyConference) args.append('e');
                if (talkerDetection) args.append('T');
                if (talkOnlyMode) args.append('t');
                if (useMusicOnHold) args.append('M');
                if (videoMode) args.append('v');
                if (waitForMarkedUser) args.append('w');
                if (StringUtils.isNotBlank(pinStr)) args.append(',').append(pinStr);
                if (debugLog.isLoggable(Level.FINEST)) debug("Executing MeetMe app with args " + args);
                int result = channel.exec("MeetMe", args.toString());
                if (debugLog.isLoggable(Level.FINEST)) debug("MeetMe return value was " + translateAppReturnValue(result));
                if (result == -1) {
                    exception = new ActionStepException("Channel was hung up");
                }
            }
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context);
    }
