    protected void writeToLogFiles(final ChatEvent event) {
        ThreadService.getInstance().run(new RaptorRunnable() {

            @Override
            public void execute() {
                if (Raptor.getInstance().getPreferences().getBoolean(PreferenceKeys.APP_IS_LOGGING_CONSOLE) && !vetoLogging(event.getSource())) {
                    appendToFile(Raptor.USER_RAPTOR_HOME_PATH + "/logs/console/" + getConnectorType() + "-console.txt", event);
                }
                if (Raptor.getInstance().getPreferences().getBoolean(PreferenceKeys.APP_IS_LOGGING_CHANNEL_TELLS) && event.getType() == ChatType.CHANNEL_TELL) {
                    appendToFile(Raptor.USER_RAPTOR_HOME_PATH + "/logs/console/" + getConnectorType() + "-" + event.getChannel() + ".txt", event);
                }
                if (Raptor.getInstance().getPreferences().getBoolean(PreferenceKeys.APP_IS_LOGGING_PERSON_TELLS) && event.getType() == ChatType.TELL && !vetoLogging(event.getSource())) {
                    appendToFile(Raptor.USER_RAPTOR_HOME_PATH + "/logs/console/" + getConnectorType() + "-" + event.getSource().toLowerCase() + ".txt", event);
                }
                if (Raptor.getInstance().getPreferences().getBoolean(PreferenceKeys.APP_IS_LOGGING_PERSON_TELLS) && event.getType() == ChatType.OUTBOUND) {
                    RaptorStringTokenizer tok = new RaptorStringTokenizer(event.getMessage(), " ", true);
                    String firstWord = tok.nextToken();
                    String secondWord = tok.nextToken();
                    if (firstWord != null && secondWord != null) {
                        if ("tell".startsWith(firstWord.toLowerCase()) && !vetoLogging(secondWord)) {
                            try {
                                Integer.parseInt(secondWord);
                            } catch (NumberFormatException nfe) {
                                appendToFile(Raptor.USER_RAPTOR_HOME_PATH + "/logs/" + getConnectorType() + "-" + secondWord.toLowerCase() + ".txt", event);
                            }
                        }
                    }
                }
            }
        });
    }
