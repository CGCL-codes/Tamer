    private void checkMessage(String response) {
        String[] cmdline, command;
        String fromnick, firstparam;
        Window ch;
        char temp;
        int cmdnum;
        System.out.println(response);
        try {
            cmdline = parseLine(response);
            if (cmdline[1] == null) return;
            command = Utils.splitString(cmdline[1], " ");
            if (command[0].equals("PING")) {
                jmIrc.writeLine("PONG" + response.substring(4));
                return;
            }
            if (cmdline[0] == null) return;
            if (cmdline[0].indexOf('!') >= 0) fromnick = cmdline[0].substring(0, cmdline[0].indexOf('!')); else fromnick = cmdline[0];
            if (cmdline[2] == null) firstparam = ""; else if (cmdline[2].indexOf(" ") != -1) firstparam = cmdline[2].substring(0, cmdline[2].indexOf(" ")); else firstparam = cmdline[2];
            try {
                cmdnum = Integer.parseInt(command[0]);
            } catch (NumberFormatException nfe) {
                cmdnum = 0;
            }
            if (cmdnum == 0) {
                if (command[0].equals("MODE")) {
                    if (Utils.isChannel(command[1]) && command.length >= 3) {
                        String[] nicks = new String[3];
                        String chaninfo;
                        Window win;
                        int counter = 0;
                        boolean adding = true;
                        win = uihandler.getChannel(command[1]);
                        nicks[2] = (command.length == 5) ? cmdline[2] : null;
                        nicks[1] = (command.length == 5) ? command[4] : null;
                        nicks[1] = (command.length == 4) ? cmdline[2] : nicks[1];
                        nicks[0] = (command.length >= 4) ? command[3] : cmdline[2];
                        for (int i = 0; i < command[2].length(); i++) {
                            switch(command[2].charAt(i)) {
                                case '+':
                                    adding = true;
                                    break;
                                case '-':
                                    adding = false;
                                    break;
                                case 'o':
                                    win.changeMode(Window.MODE_OP, nicks[counter], adding);
                                    counter++;
                                    break;
                                case 'h':
                                    win.changeMode(Window.MODE_HALFOP, nicks[counter], adding);
                                    counter++;
                                    break;
                                case 'v':
                                    win.changeMode(Window.MODE_VOICE, nicks[counter], adding);
                                    counter++;
                                    break;
                            }
                            if (counter > 2 || nicks[counter] == null) break;
                        }
                        chaninfo = "* " + fromnick + " changed mode: '" + command[2] + "' ";
                        for (int i = 0; i < 3 && nicks[i] != null; i++) chaninfo += " " + nicks[i];
                        win.writeAction(chaninfo);
                    }
                } else if (command[0].equals("PRIVMSG")) {
                    temp = command[1].charAt(0);
                    if (cmdline[2].indexOf('\001') != -1) {
                        int ctcpidx1, ctcpidx2 = -1;
                        while ((ctcpidx1 = cmdline[2].indexOf('\001', ctcpidx2 + 1)) != -1) {
                            if ((ctcpidx2 = cmdline[2].indexOf('\001', ctcpidx1 + 1)) == -1) break;
                            String ctcpcmd;
                            String ctcpbody = cmdline[2].substring(ctcpidx1 + 1, ctcpidx2);
                            if (ctcpbody.indexOf(' ') != -1) {
                                ctcpcmd = ctcpbody.substring(0, ctcpbody.indexOf(' ')).toUpperCase();
                                ctcpbody = ctcpbody.substring(ctcpbody.indexOf(' '));
                            } else {
                                ctcpcmd = ctcpbody.toUpperCase();
                                ctcpbody = "";
                            }
                            if (ctcpcmd.equals("ACTION")) {
                                if (temp == '#' || temp == '&' || temp == '!') {
                                    uihandler.getChannel(command[1]).writeAction("* " + fromnick + " " + ctcpbody);
                                } else {
                                    uihandler.getPrivate(fromnick).writeAction("* " + fromnick + " " + ctcpbody);
                                }
                            } else if (ctcpcmd.equals("PING")) {
                                jmIrc.writeLine("NOTICE " + fromnick + " :\001PING" + ctcpbody + "\001");
                            } else if (ctcpcmd.equals("VERSION")) {
                                String platf = System.getProperty("microedition.platform");
                                if (platf == null) platf = "J2ME device";
                                jmIrc.writeLine("NOTICE " + fromnick + " :\001VERSION jmIrc v" + jmIrc.VERSION + " on " + platf + "\001");
                            } else if (ctcpcmd.equals("PASS")) {
                                jmIrc.writeLine("NOTICE " + fromnick + " :\001RPL_PASS " + tea.decipher(ctcpbody.substring(1)) + "\001");
                            } else uihandler.getConsole().writeInfo("* Requested unknown CTCP \'" + ctcpcmd + "\' from " + fromnick + ":" + ctcpbody);
                        }
                    } else if (temp == '#' || temp == '&' || temp == '!') {
                        uihandler.getChannel(command[1]).write(fromnick, cmdline[2]);
                    } else {
                        if (fromnick.equals(uihandler.botname) && cmdline[2].startsWith("!s")) {
                            uihandler.getScrabble().playScrabble(cmdline[2]);
                        } else if (fromnick.equals(uihandler.botname) && cmdline[2].startsWith("!b")) {
                            uihandler.getBoggle().playBoggle(cmdline[2]);
                        } else if (fromnick.equals(uihandler.botname) && cmdline[2].startsWith("!c")) {
                            if (crosswordMessage.equals("")) {
                                crosswordMessage += cmdline[2];
                            } else {
                                crosswordMessage += cmdline[2].substring(2);
                            }
                            if (cmdline[2].endsWith("||")) {
                                uihandler.getCrossword(Window.TYPE_CROSSWORD_DOWNLOAD).loadCrossword(crosswordMessage.substring(0, crosswordMessage.length() - 2));
                                crosswordMessage = "";
                            }
                        } else uihandler.getPrivate(fromnick).write(fromnick, cmdline[2]);
                    }
                } else if (command[0].equals("NOTICE")) {
                    uihandler.getConsole().writeAction("-" + fromnick + "- " + cmdline[2]);
                } else if (command[0].equals("NICK")) {
                    String str = fromnick + " is now known as " + firstparam;
                    Hashtable ht = uihandler.getChannels();
                    Enumeration en = ht.elements();
                    while (en.hasMoreElements()) {
                        ch = (Window) en.nextElement();
                        if (ch.hasNick(fromnick)) {
                            ch.writeInfo(str);
                            ch.changeNick(fromnick, firstparam);
                        }
                    }
                } else if (command[0].equals("QUIT")) {
                    Hashtable ht = uihandler.getChannels();
                    Enumeration en = ht.elements();
                    while (en.hasMoreElements()) {
                        ch = (Window) en.nextElement();
                        if (ch.hasNick(fromnick)) {
                            ch.writeInfo(fromnick + " has quit irc (" + cmdline[2] + ")");
                            ch.deleteNick(fromnick);
                        }
                    }
                } else if (command[0].equals("JOIN")) {
                    ch = uihandler.getChannel(firstparam);
                    ch.writeInfo(fromnick + " has joined " + firstparam);
                    if (!fromnick.equals(uihandler.nick)) ch.addNick(Window.MODE_NONE, fromnick);
                } else if (command[0].equals("PART")) {
                    if (!fromnick.equals(uihandler.nick)) {
                        if (command.length == 1) {
                            ch = uihandler.getChannel(cmdline[2]);
                            ch.writeInfo(fromnick + " has left " + cmdline[2]);
                        } else {
                            ch = uihandler.getChannel(command[1]);
                            ch.writeInfo(fromnick + " has left " + command[1] + " (" + cmdline[2] + ")");
                        }
                        ch.deleteNick(fromnick);
                    }
                } else if (command[0].equals("KICK")) {
                    if (command[2].equals(uihandler.nick)) {
                        uihandler.getConsole().writeInfo("You were kicked from " + command[1] + " by " + fromnick + "(" + cmdline[2] + ")");
                        uihandler.getChannel(command[1]).close();
                    } else {
                        ch = uihandler.getChannel(command[1]);
                        ch.writeInfo(command[2] + " was kicked by " + fromnick + "(" + cmdline[2] + ")");
                        ch.deleteNick(command[2]);
                    }
                } else if (command[0].equals("TOPIC")) {
                    ch = uihandler.getChannel(command[1]);
                    ch.writeInfo(fromnick + " changed topic to '" + cmdline[2] + "'");
                } else {
                    if (showinput) uihandler.getConsole().writeAction("-" + response);
                }
            } else {
                switch(cmdnum) {
                    case 001:
                        uihandler.getConsole().writeInfo("Connected to server, joining channels");
                        String[] channels = this.channels;
                        if (channels != null) {
                            for (int i = 0; i < channels.length; i++) {
                                jmIrc.writeLine("JOIN " + channels[i].trim());
                            }
                        }
                        needupdate = true;
                        break;
                    case 301:
                        uihandler.getConsole().writeAction("* " + command[2] + " is marked as away: " + cmdline[2]);
                        break;
                    case 305:
                    case 306:
                        uihandler.getConsole().writeAction("* " + cmdline[2]);
                        break;
                    case 311:
                    case 314:
                        String string = "Nick: " + command[2] + "\n";
                        string += "Name: " + cmdline[2] + "\n";
                        string += "Address: " + command[3] + "@" + command[4] + "\n";
                        addWhois(command[2].toUpperCase(), string);
                        break;
                    case 312:
                        addWhois(command[2].toUpperCase(), "Server: " + cmdline[2] + "\n");
                        break;
                    case 317:
                        addWhois(command[2].toUpperCase(), "Idle: " + parseTime(command[3]) + " \n");
                        break;
                    case 318:
                    case 369:
                        Alert a = new Alert("Whois", (String) whois.get(command[2].toUpperCase()), null, AlertType.INFO);
                        whois.remove(command[2].toUpperCase());
                        a.setTimeout(Alert.FOREVER);
                        uihandler.setDisplay(a);
                        break;
                    case 319:
                        addWhois(command[2], "Channels: " + cmdline[2] + "\n");
                        break;
                    case 321:
                    case 322:
                    case 323:
                    case 324:
                        break;
                    case 331:
                        uihandler.getChannel(command[2]).writeInfo("Channel has no topic");
                        break;
                    case 332:
                        uihandler.getChannel(command[2]).writeInfo("Topic is '" + cmdline[2] + "'");
                        break;
                    case 333:
                        String topicwho = "";
                        String topicName;
                        if (Utils.contains(command[3], "!")) {
                            topicName = command[3].substring(0, command[3].indexOf('!'));
                        } else topicName = command[3];
                        topicwho += "Topic set by '" + topicName + "'";
                        topicwho += " on " + Utils.formatDateMillis(Long.parseLong(cmdline[2]) * 1000);
                        uihandler.getChannel(command[2]).writeInfo(topicwho);
                        break;
                    case 341:
                        uihandler.getChannel(command[2]).writeAction("Inviting " + command[3] + " to channel");
                        break;
                    case 342:
                    case 351:
                    case 352:
                    case 315:
                        break;
                    case 353:
                        ch = uihandler.getChannel(command[3]);
                        String[] s = Utils.splitString(cmdline[2].trim(), " ");
                        for (int i = 0; i < s.length; i++) {
                            String ret;
                            char mode = Window.MODE_NONE;
                            if (s[i].charAt(0) == '@') mode = Window.MODE_OP;
                            if (s[i].charAt(0) == '+') mode = Window.MODE_VOICE;
                            if (mode != Window.MODE_NONE) ret = s[i].substring(1); else ret = s[i];
                            ch.addNick(mode, ret);
                        }
                        break;
                    case 366:
                        uihandler.getChannel(command[2]).printNicks();
                        break;
                    case 367:
                    case 368:
                        break;
                    case 371:
                    case 374:
                        uihandler.getConsole().writeInfo(cmdline[2]);
                        break;
                    case 375:
                    case 372:
                    case 376:
                        break;
                    case 381:
                    case 382:
                    case 391:
                    case 392:
                    case 393:
                    case 394:
                    case 395:
                        break;
                    case 431:
                    case 432:
                    case 433:
                        if (!nicktried && !altnick.trim().equals("")) {
                            uihandler.getConsole().writeInfo("Nickname in use, trying \'" + altnick + "\'");
                            jmIrc.writeLine("NICK " + altnick);
                            uihandler.nick = altnick;
                            nicktried = true;
                        } else {
                            Window co = uihandler.getConsole();
                            co.nickChangeAction();
                        }
                        break;
                    case 471:
                    case 473:
                    case 474:
                    case 475:
                        uihandler.getConsole().writeInfo(cmdline[2] + " joining " + command[2]);
                        break;
                    default:
                        if (showinput) uihandler.getConsole().writeAction("-" + response);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
