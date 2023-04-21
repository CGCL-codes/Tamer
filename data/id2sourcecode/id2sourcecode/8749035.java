                            public boolean onNewEventParsed(ChatEvent event) {
                                if (event.getType() == ChatType.CHANNEL_TELL && StringUtils.equals(event.getChannel(), whatsLeft)) {
                                    builder.append(FORMAT.format(new Date(event.getTime()))).append(event.getMessage().trim()).append("\n");
                                }
                                return true;
                            }
