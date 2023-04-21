                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                    }
                    for (int i = 0; i < channel.length; i++) {
                        System.out.println("ensuring join " + channel[i]);
                        if (!bot.ensureJoin(channel[i])) {
                            bot.leaveChannel(bot.getChannelId(channel[i]), true);
                        }
                    }
                }
