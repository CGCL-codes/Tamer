    @Override
    public void processLocalCommand(String input) {
        if (input.equalsIgnoreCase("DISCONNECT") || input.equalsIgnoreCase("QUIT")) {
            disconnect();
        } else if (input.toUpperCase().startsWith("MOTD")) {
            if (input.length() == 4) {
                terminal.writeTo("\nSet the Message of the Day to what?");
            } else if (input.length() == 5 && input.charAt(4) == ' ') {
                terminal.writeTo("\nSet the Message of the Day to what?");
            } else if (input.length() > 5 && input.charAt(4) == ' ') {
                setMOTD(Colour.colourise(input.substring(5), Colour.yellow) + Colour.colourise("", Colour.grey));
            }
        } else if (input.toUpperCase().startsWith("SERVERMSG")) {
            if (input.length() == 9) {
                terminal.writeTo("\nYou need to enter a server message to broadcast.");
            } else if (input.length() == 10 && input.charAt(9) == ' ') {
                terminal.writeTo("\nYou need to enter a server message to broadcast.");
            } else if (input.length() > 10 && input.charAt(9) == ' ') {
                broadcaster.broadcastServerMessage(input.substring(10));
                terminal.writeTo("\nSERVERMESSAGE sent: '" + Colour.colourise(input.substring(10), Colour.white) + Colour.colourise("'", Colour.grey));
            }
        } else if (input.toUpperCase().startsWith("CHANNELJOIN")) {
            if (input.length() == 11) {
                terminal.writeTo("\nChannel not specified.\nSyntax: channeljoin <channelipaddress>");
            } else if (input.length() == 12 && input.charAt(11) == ' ') {
                terminal.writeTo("\nChannel not specified.\nSyntax: channeljoin <channelipaddress>");
            } else if (input.length() > 12 && input.charAt(11) == ' ') {
                String channelIP = input.substring(12).trim();
                setChannel(channelIP);
            }
        } else if (input.toUpperCase().startsWith("SERVERJOIN")) {
            if (input.length() == 11 || (input.length() == 12 && input.charAt(11) == ' ') || (input.length() > 12 && input.charAt(11) == ' ')) {
                terminal.writeTo("\nYou are currently hosting a server in this terminal. " + "If you wish to join a server, please open a new instance of WITNA " + "or disconnect the server first.");
            }
        } else if (input.equalsIgnoreCase("SAVE")) {
            if (players.savePlayers()) {
                terminal.writeTo("\n" + Colour.colourise("Player status saved.", Colour.green) + Colour.colourise("", Colour.grey));
            } else {
                terminal.writeTo("\nERROR: Player status not saved. Cannot create file. Ensure WITNA is not installed to a read-only directory.");
            }
        } else if (input.startsWith("HELP")) {
            if (input.length() == 4) {
                help("");
            } else if (input.length() == 5 && input.charAt(11) == ' ') {
                help("");
            } else if (input.length() > 5 && input.charAt(11) == ' ') {
                help(input.substring(5));
            }
        } else if (input.startsWith("?")) {
            if (input.length() == 1) {
                help("");
            } else if (input.length() == 2 && input.charAt(11) == ' ') {
                help("");
            } else if (input.length() > 2 && input.charAt(11) == ' ') {
                help(input.substring(2));
            }
        } else {
            terminal.writeTo("\nServer command not recognised.");
        }
    }
