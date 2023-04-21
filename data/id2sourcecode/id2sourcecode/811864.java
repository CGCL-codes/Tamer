    public void getConfig() {
        String line;
        in = new Input(configfile);
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("group[")) {
                group.add(new Group());
                ((Group) group.get(group.size() - 1)).setGroupName(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
            }
        }
        in.close();
        in = new Input(configfile);
        line = "";
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.indexOf("=") != -1) {
                String fPart = line.substring(0, line.indexOf("="));
                String mPart = line.substring(line.indexOf("="), line.indexOf("=") + 1);
                String lPart = line.substring(line.indexOf("=") + 1);
                line = fPart.trim() + mPart.trim() + lPart.trim();
            }
            if (!(line.startsWith("/"))) {
                if (line.startsWith("nick[") && line.charAt(7) != '[') {
                    nick[Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")))] = line.substring(line.indexOf("=") + 1);
                    if (debug) {
                        System.err.println("nick[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "]: " + nick[Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")))]);
                    }
                } else if (line.startsWith("nickPass[") && line.charAt(11) != '[') {
                    nickPass[Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")))] = line.substring(line.indexOf("=") + 1);
                    if (debug) {
                        System.err.println("nickPass[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "]: " + nickPass[Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")))]);
                    }
                } else if (line.startsWith("username=")) {
                    username = line.substring(line.indexOf("=") + 1);
                    if (debug) {
                        System.err.println("username: " + username);
                    }
                } else if (line.startsWith("realname=")) {
                    realname = line.substring(line.indexOf("=") + 1);
                    if (debug) {
                        System.err.println("realname: " + realname);
                    }
                } else if (line.startsWith("localhost=")) {
                    localhost = line.substring(line.indexOf("=") + 1);
                    if (debug) {
                        System.err.println("localhost: " + localhost);
                    }
                } else if (line.startsWith("size=")) {
                    width = Integer.parseInt(line.substring(line.indexOf("=") + 1, line.indexOf("x")));
                    height = Integer.parseInt(line.substring(line.indexOf("x") + 1));
                    if (debug) {
                        System.err.println("width: " + width);
                        System.err.println("height: " + height);
                    }
                } else if (line.startsWith("server[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).addServer(line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("server[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "][" + Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getServers().get(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1)))));
                    }
                } else if (line.startsWith("port[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).addPort(Integer.parseInt(line.substring(line.indexOf("=") + 1)));
                    if (debug) {
                        System.err.println("port[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "][" + Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getPorts().get(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1)))));
                    }
                } else if (line.startsWith("serverPass[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).addServerPass(line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("serverPass[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "][" + Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getServerPasses().get(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1)))));
                    }
                } else if (line.startsWith("charset[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).setCharset(line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("charset[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getCharset());
                    }
                } else if (line.startsWith("channel[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).addChannel(line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("channel[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "][" + Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getChannels().get(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1)))));
                    }
                } else if (line.startsWith("nick[") && line.charAt(7) == '[') {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).setNick(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))), line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("nick[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "][" + Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getNick(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1)))));
                    }
                } else if (line.startsWith("nickPass[") && line.charAt(11) == '[') {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).setNickPass(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))), line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("nickPass[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "][" + Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getNickPass(Integer.parseInt(line.substring(line.indexOf("[", line.indexOf("[") + 1) + 1, line.indexOf("]", line.indexOf("]") + 1)))));
                    }
                } else if (line.startsWith("username[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).setUsername(line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("username[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getUsername());
                    }
                } else if (line.startsWith("realname[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).setRealname(line.substring(line.indexOf("=") + 1));
                    if (debug) {
                        System.err.println("realname[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getRealname());
                    }
                } else if (line.startsWith("autoConnect[")) {
                    ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).setAutoConnect(Boolean.parseBoolean(line.substring(line.indexOf("=") + 1)));
                    if (debug) {
                        System.err.println("autoConnect[" + Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))) + "]: " + ((Group) group.get(Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))).getAutoConnect());
                    }
                }
            }
        }
        in.close();
    }
