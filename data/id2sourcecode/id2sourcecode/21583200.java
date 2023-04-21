    public static void main(String[] args) {
        try {
            final IrcBot2rc bot = new IrcBot2rc();
            ZorobotProperties props = ZorobotSystem.props;
            bot.setDefaultNickname(props.getProperty("irc.nick"));
            bot.setDefaultFullname("Roronoa_2oro's Bot: zorobot 08");
            bot.setDefaultUsername(props.getProperty("irc.fullname"));
            be.trc.core.IRCServer server = bot.connect(props.getProperty("irc.server"), Integer.parseInt(props.getProperty("irc.port")));
            Thread.sleep(2000);
            System.out.println(bot.isConnected(server));
            String nickPass = props.getProperty("irc.password");
            if (nickPass != null && !"".equals(nickPass)) {
                Thread.sleep(5000);
                bot.sendMessage("nickserv", "IDENTIFY " + nickPass, server);
            }
            String channels = props.getProperty("irc.channel");
            final String channel[] = channels.split(",");
            long joinDelay = 10;
            try {
                joinDelay = Long.parseLong(props.getProperty("irc.joindelay"));
            } catch (Exception eeee) {
            }
            Thread.sleep(joinDelay * 1000);
            bot.setNumOfFixedChannel(channel.length);
            bot.setServer(server);
            for (int i = 0; i < channel.length; i++) {
                bot.joinChannel(channel[i]);
                System.out.println("join " + channel[i]);
            }
            new Thread() {

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
            }.start();
            iThread.bot = bot;
            iThread.start();
            oThread.bot = bot;
            oThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
