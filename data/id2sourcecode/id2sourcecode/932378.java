    public void parse_command(Core C, Q Bot, DBControl dbc, String numeric, String botnum, String target, String username, String params) {
        String user[] = dbc.getUserRow(username);
        if (user[4].equals("0")) {
            C.cmd_notice(numeric, botnum, username, "You are not AUTH'd.");
            return;
        }
        String auth[] = dbc.getAuthRow(user[4]);
        if (user[5].equals("1") && Integer.parseInt(auth[3]) > 99) {
            String[] result = params.split("\\s");
            try {
                String chan = result[1];
                if (dbc.chanExists(chan)) {
                    C.cmd_notice(numeric, botnum, username, "Channel is registerd so chanfix cannot be used.");
                    return;
                }
                C.cmd_privmsg(numeric, botnum, chan, "ChanFix was orderd to fix this channel by " + user[1] + ".");
                String users[] = dbc.getChannelUsers(chan);
                for (int n = 0; n < users.length; n++) {
                    String userinfo[] = dbc.getUserRow(users[n]);
                    String userid = userinfo[2];
                    if (!userinfo[4].equalsIgnoreCase("0")) {
                        userid = userinfo[4];
                    }
                    if (dbc.isKnownOpChan(userid, chan)) {
                        C.cmd_mode(numeric, users[n], chan, "+o");
                    } else {
                        C.cmd_mode(numeric, users[n], chan, "-o");
                    }
                }
                C.cmd_privmsg(numeric, botnum, chan, "Done.");
                C.cmd_notice(numeric, botnum, username, "Done.");
            } catch (Exception e) {
                C.cmd_notice(numeric, botnum, username, "/msg " + Bot.get_nick() + " chanfix <#channel>");
            }
            return;
        } else {
            C.cmd_notice(numeric, botnum, username, "This command is either unknown, or you need to be opered up to use it.");
            return;
        }
    }
