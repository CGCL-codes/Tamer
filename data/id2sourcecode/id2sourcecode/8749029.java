    @Override
    public RaptorAliasResult apply(final ChatConsoleController controller, String command) {
        command = command.trim();
        if (StringUtils.startsWith(command, "=tells")) {
            final String whatsLeft = command.length() == 6 ? "" : command.substring(7).trim();
            if (whatsLeft.contains(" ")) {
                return new RaptorAliasResult(null, "Invalid command: " + command + ".\n" + getUsage());
            } else if (StringUtils.isBlank(whatsLeft)) {
                ThreadService.getInstance().run(new Runnable() {

                    public void run() {
                        final StringBuilder builder = new StringBuilder(5000);
                        controller.getConnector().getChatService().getChatLogger().parseFile(new ChatEventParseListener() {

                            public boolean onNewEventParsed(ChatEvent event) {
                                if (event.getType() == ChatType.TELL) {
                                    builder.append(FORMAT.format(new Date(event.getTime()))).append(event.getMessage().trim()).append("\n");
                                }
                                return true;
                            }

                            public void onParseCompleted() {
                                Raptor.getInstance().getDisplay().asyncExec(new RaptorRunnable(controller.getConnector()) {

                                    @Override
                                    public void execute() {
                                        controller.onAppendChatEventToInputText(new ChatEvent(null, ChatType.INTERNAL, "All direct tells sent since you logged in:\n" + builder));
                                    }
                                });
                            }
                        });
                    }
                });
                return new RaptorAliasResult(null, "Your request is being processed. This may take a moment");
            } else if (NumberUtils.isDigits(whatsLeft)) {
                ThreadService.getInstance().run(new Runnable() {

                    public void run() {
                        final StringBuilder builder = new StringBuilder(5000);
                        controller.getConnector().getChatService().getChatLogger().parseFile(new ChatEventParseListener() {

                            public boolean onNewEventParsed(ChatEvent event) {
                                if (event.getType() == ChatType.CHANNEL_TELL && StringUtils.equals(event.getChannel(), whatsLeft)) {
                                    builder.append(FORMAT.format(new Date(event.getTime()))).append(event.getMessage().trim()).append("\n");
                                }
                                return true;
                            }

                            public void onParseCompleted() {
                                Raptor.getInstance().getDisplay().asyncExec(new Runnable() {

                                    public void run() {
                                        controller.onAppendChatEventToInputText(new ChatEvent(null, ChatType.INTERNAL, "All " + whatsLeft + " tells sent since you logged in:\n" + builder));
                                    }
                                });
                            }
                        });
                    }
                });
                return new RaptorAliasResult(null, "Your request is being processed. This may take a moment");
            } else {
                ThreadService.getInstance().run(new Runnable() {

                    public void run() {
                        final StringBuilder builder = new StringBuilder(5000);
                        controller.getConnector().getChatService().getChatLogger().parseFile(new ChatEventParseListener() {

                            public boolean onNewEventParsed(ChatEvent event) {
                                if (event.getType() == ChatType.TELL && StringUtils.startsWithIgnoreCase(event.getSource(), whatsLeft)) {
                                    builder.append(FORMAT.format(new Date(event.getTime()))).append(event.getMessage().trim()).append("\n");
                                }
                                return true;
                            }

                            public void onParseCompleted() {
                                Raptor.getInstance().getDisplay().asyncExec(new RaptorRunnable(controller.getConnector()) {

                                    @Override
                                    public void execute() {
                                        controller.onAppendChatEventToInputText(new ChatEvent(null, ChatType.INTERNAL, "All " + whatsLeft + " tells sent since you logged in:\n" + builder));
                                    }
                                });
                            }
                        });
                    }
                });
                return new RaptorAliasResult(null, "Your request is being processed. This may take a moment");
            }
        }
        return null;
    }
