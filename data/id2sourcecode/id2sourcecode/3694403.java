            public Boolean call() {
                long now = System.currentTimeMillis();
                for (ChannelConfig c : channelMap.values()) {
                    if (c.isListAllowed()) if (Arrays.asList(settings.theBot.getChannels()).contains(c.getName()) && now > c.getListStamp() + 14400000) {
                        c.setListStamp(now);
                        settings.theBot.sendMessage(c.getName(), "!list");
                        tfinder.setListChannel(c.getName());
                        synchronized (this) {
                            try {
                                wait(120000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
                return true;
            }
