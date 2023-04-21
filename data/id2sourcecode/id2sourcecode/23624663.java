            @Override
            public boolean handle(NewMinute message) {
                gameTimeHour = message.getHour();
                gameTimeMinute = message.getMinute();
                if (getGameDate() == "Uninitialized") {
                    send(new Message(Protocol.GET_DATE));
                }
                if (gameTimeHour == 6 && gameTimeMinute == 0) {
                    dayDesc = null;
                    send(new Message(Protocol.GET_DATE));
                    send(new Message(Protocol.RAW_TEXT, "#day"));
                }
                if (client != null && gameTimeHour == 5 && (gameTimeMinute > 54 || gameTimeMinute == 45 || gameTimeMinute == 30)) {
                    for (GuildMember m : data.getGuild().members) {
                        if (m.isListenToChannel(getChannelId("#day"))) {
                            client.sendMessage(m.jid, "New day will start in " + (60 - gameTimeMinute) + " minutes");
                        }
                    }
                }
                if (gameTimeHour == 5 && gameTimeMinute == 59 && data.getConfig().boardcastDayEnd) {
                    send(new Message(Protocol.SIT_DOWN, new Uint8(0)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                    sendGuildMessage("New day will start in 1 minute.");
                    sendEmote(Emote.CheerRight);
                }
                return false;
            }
