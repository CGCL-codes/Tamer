    public void handle_command(Service who, String user, String replyto, String arguments) {
        String args[] = arguments.split(" ");
        if (args.length > 3) {
            String what = args[0];
            String what2 = args[1];
            String cmd = args[2];
            String towhat = args[3];
            Boolean confirm = false;
            String reason = "";
            if (args.length == 5) confirm = args[4].equals("CONFIRM");
            if (args.length > 5) for (int i = 5; i < args.length; i++) reason = reason + args[i] + " "; else reason = "Matched Grep.";
            try {
                if (what.equals("users")) {
                    List<Client> matches = new ArrayList<Client>();
                    for (Map.Entry<String, Client> Client : Generic.Users.entrySet()) {
                        if (what2.equals("uid")) {
                            if (Client.getValue().uid.matches(towhat.replace("*", "\\S+"))) matches.add(Client.getValue());
                        } else if (what2.equals("mask")) {
                            if (Client.getValue().host.matches(towhat.replace("*", "\\S+"))) matches.add(Client.getValue());
                        } else if (what2.equals("ident")) {
                            if (Client.getValue().ident.matches(towhat.replace("*", "\\S+"))) matches.add(Client.getValue());
                        } else {
                            Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid field for user. Usage: grep users [uid|mask|ident] [print|kill|gline] regexp");
                            return;
                        }
                    }
                    if (cmd.equals("count")) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Grep Results: " + matches.size() + " matches.");
                    } else if (cmd.equals("print")) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Grep Results: " + matches.size() + " matches.");
                        for (int i = 0; i < matches.size(); i++) Generic.curProtocol.outPRVMSG(who, replyto, "   " + matches.get(i).toString());
                    } else if (cmd.equals("kill")) {
                        if (matches.size() > (Generic.Users.size() / 10)) {
                            if (confirm) {
                                for (int i = 0; i < matches.size(); i++) {
                                    Generic.curProtocol.outKILL(who, ((Client) matches.get(i)).uid, reason);
                                }
                                Logging.warn("SRVOPER", user + " killed more than 10% of the network.!");
                            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Affects more than 10% of network (" + matches.size() + "/" + Generic.Users.size() + ", " + (((float) matches.size() / (float) Generic.Users.size()) * 100) + "%). Confirm by adding CONFIRM to end of command.");
                        } else {
                            for (int i = 0; i < matches.size(); i++) Generic.curProtocol.outKILL(who, ((Client) matches.get(i)).uid, reason);
                        }
                    } else if (cmd.equals("gline")) {
                        if (matches.size() > (Generic.Users.size() / 10)) {
                            if (confirm) {
                                for (int i = 0; i < matches.size(); i++) {
                                    Generic.curProtocol.outGLINE(who, ((Client) matches.get(i)), reason);
                                }
                                Logging.warn("SRVOPER", user + " managed to gline more than 10% of the network!");
                            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Affects more than 10% of network (" + matches.size() + "/" + Generic.Users.size() + ", " + ((matches.size() / Generic.Users.size()) * 100) + "%). Confirm by adding CONFIRM to end of command.");
                        } else {
                            for (int i = 0; i < matches.size(); i++) Generic.curProtocol.outGLINE(who, ((Client) matches.get(i)), reason);
                        }
                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid action for user. Usage: grep users [uid|mask|ident] [print|count|kill|gline] regexp");
                } else if (what.equals("channels")) {
                    List<Channel> matches = new ArrayList<Channel>();
                    for (Map.Entry<String, Channel> Client : Generic.Channels.entrySet()) {
                        if (what2.equals("name")) {
                            if (Client.getValue().name.matches(towhat.replace("*", "\\S+"))) matches.add(Client.getValue());
                        } else if (what2.equals("topic")) {
                            if (Client.getValue().topic.matches(towhat.replace("*", "\\S+"))) matches.add(Client.getValue());
                        } else {
                            Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid field for channel. Usage: grep channels [name|topic] [print|count|close] regexp");
                            return;
                        }
                    }
                    if (cmd.equals("count")) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Grep Results: " + matches.size() + " matches.");
                    } else if (cmd.equals("print")) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Grep Results: " + matches.size() + " matches.");
                        for (int i = 0; i < matches.size(); i++) Generic.curProtocol.outPRVMSG(who, replyto, "   " + matches.get(i).toString());
                    } else if (cmd.equals("close")) {
                        for (int i = 0; i < matches.size(); i++) {
                            ArrayList<Client> alc = new ArrayList<Client>();
                            for (Map.Entry<Client, UserModes> Cl : ((Channel) matches.get(i)).clientmodes.entrySet()) alc.add(Cl.getKey());
                            for (Client c : alc) {
                                if (!c.modes.contains("o")) {
                                    if (c.authhandle != null) Generic.curProtocol.outKICK(who, c.uid, ((Channel) matches.get(i)).name, "Channel has been closed by network administration."); else Generic.curProtocol.outKILL(who, c.uid, "Channel " + ((Channel) matches.get(i)).name + " has been closed by network staff.");
                                }
                            }
                            ((SrvChannel) who).getChannels().get(((Channel) matches.get(i)).name).setMeta("_isbad", "Channel was closed via batch grep.");
                            Logging.warn("SRVOPER", user + " closed channel " + ((Channel) matches.get(i)).name + " !");
                        }
                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid action for channels. Usage: grep channels [name|topic] [print|count|close] regexp");
                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid command. Usage: grep [users|channels] [(uid|ident|mask)(name|topic)] [print|count|(kill|gline)(close)] regexp");
            } catch (PatternSyntaxException pse) {
                Generic.curProtocol.outPRVMSG(who, replyto, "Error: Bad Regex.");
            }
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid arguments. Usage: grep [users|channels] [(uid|ident|mask)(name|topic)] [print|count|(kill|gline)(close)] regexp");
    }
