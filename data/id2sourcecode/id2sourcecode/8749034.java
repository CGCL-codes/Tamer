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
