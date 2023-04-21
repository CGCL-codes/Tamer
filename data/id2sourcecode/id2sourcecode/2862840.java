    private void OwnerCommandHandler(String source, String message) {
        String[] Cmd = message.split(" ");
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "disconnect")) {
            this.disconnect();
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "shutdown")) {
            super.sendMessage(source, "Bye Bye :-(");
            _Mngr.Shutdown();
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "reconnect")) {
            try {
                super.reconnect();
            } catch (NickAlreadyInUseException e) {
                LOGGER.error("Nick is Already in Use!");
            } catch (IOException e) {
                LOGGER.error("IOException, This usually means your net is down or something.");
            } catch (IrcException e) {
                LOGGER.error("IRC Exception, This usually happens when the IRC server hates you :-(.");
            }
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "disconnect")) {
            this.disconnect();
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "join") && Cmd.length > 1) {
            super.joinChannel(Cmd[1]);
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "part") && Cmd.length > 1) {
            super.partChannel(Cmd[1]);
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "statcpu")) {
            super.sendMessage(source, "Available Processors (Cores): " + Runtime.getRuntime().availableProcessors());
        }
        if (Cmd[0].equalsIgnoreCase(CMDPREFIX + "stat")) {
            if (Cmd.length > 1 && Cmd[1].equalsIgnoreCase("channels")) {
                StringBuffer tmpBuffer = new StringBuffer("");
                String[] tmp = super.getChannels();
                for (int i = 0; i < tmp.length; i++) {
                    tmpBuffer.append(tmp[i]);
                    if (i < tmp.length - 1) {
                        tmpBuffer.append(",");
                    }
                }
                super.sendMessage(source, "Current Channels: " + tmpBuffer.toString());
            } else if (Cmd.length > 1 && Cmd[1].equalsIgnoreCase("watcher")) {
                super.sendMessage(source, "(Announce Channel | Announcer)");
                super.sendMessage(source, "(" + SETTINGS.getAnnounceChannel() + "|" + SETTINGS.getAnnouncerNick() + ")");
            } else {
                super.sendMessage(source, "Stat requires more arguments.");
            }
        }
    }
