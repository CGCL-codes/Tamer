    private void hostChannel(StringTokenizer tokens, boolean master) {
        int port = 724;
        if (master) {
            port = 725;
        }
        if (portFree(port, true)) {
            if (tokens.hasMoreTokens()) {
                if (master) {
                    if (tokens.countTokens() == 2) {
                        String user = tokens.nextToken();
                        String pass = tokens.nextToken();
                        channel = new MasterChannelImp(user, pass, terminal);
                    } else {
                        terminal.writeTo("\nYou must specify the admin username and password.\n" + "Syntax: masterchannelhost username password");
                    }
                } else {
                    String name = tokens.nextToken();
                    while (tokens.hasMoreTokens()) {
                        name = name.concat(" ".concat(tokens.nextToken()));
                    }
                    channel = new ChannelImp(name, terminal);
                }
                channel.start();
                hostingChannel = true;
            } else {
                terminal.writeTo("\nYou must specify the channel name.\n" + "Syntax: channelhost Sample Server Name");
            }
        } else {
            terminal.writeTo("\nERROR: Port " + port + " is already in use. Cannot host channel.");
        }
    }
