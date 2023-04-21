    public void run() {
        ServerConnection server = null;
        JBouncer bouncer = null;
        User user = null;
        boolean replayHistory = false;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String nick = null;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("NICK ")) {
                    nick = line.substring(5);
                    break;
                }
            }
            if (nick == null) {
                return;
            }
            sendRawLine(":jbouncer 001 " + nick + " :Welcome to JBouncer: http://www.jibble.org/jbouncer/");
            sendRawLine(":jbouncer 002 " + nick + " :This is an IRC proxy/bouncer. Unauthorized users must disconnect immediately.");
            sendRawLine(":jbouncer 003 " + nick + " :This bouncer has been up since " + new Date(manager.getStartTime()));
            sendRawLine(METHOD + "To connect, enter your password by typing " + Colors.BOLD + "/msg " + NICK + " " + Colors.UNDERLINE + "login" + Colors.UNDERLINE + " " + Colors.UNDERLINE + "password");
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("privmsg " + NICK + " :")) {
                    line = line.substring(("privmsg " + NICK + " :").length());
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        String login = parts[0];
                        String password = parts[1];
                        user = new User(login, password);
                        bouncer = manager.getBouncer(user);
                        if (bouncer != null) {
                            break;
                        }
                        JBouncerManager.log("Failed login attempt from " + socket.getInetAddress().getHostName() + " (" + user.getLogin() + "/" + user.getPassword() + ")");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    sendRawLine(METHOD + "Invalid login or password.");
                }
            }
            if (line == null) {
                return;
            }
            JBouncerManager.log(user.getLogin() + " logged in successfully from " + socket.getInetAddress().getHostName());
            boolean gettingInput = true;
            while (gettingInput) {
                HashMap servers = bouncer.getServers();
                synchronized (servers) {
                    if (servers.size() > 0) {
                        sendRawLine(METHOD + "Attach to one of the following servers by typing " + Colors.BOLD + "/msg " + NICK + " connect " + Colors.UNDERLINE + "name");
                        Iterator it = servers.keySet().iterator();
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            server = (ServerConnection) servers.get(key);
                            String status = " (" + server.getClientCount() + " clients)";
                            if (!server.isConnected()) {
                                status = " [disconnected]";
                            }
                            sendRawLine(METHOD + "    " + Colors.BOLD + key + Colors.BOLD + ": " + server.getNick() + " on " + server.getServer() + ":" + server.getPort() + status);
                        }
                    }
                }
                sendRawLine(METHOD + "Create a new IRC server connection by typing " + Colors.BOLD + "/msg " + NICK + " create " + Colors.UNDERLINE + "name" + Colors.UNDERLINE + " " + Colors.UNDERLINE + "server" + Colors.UNDERLINE + " " + Colors.UNDERLINE + "[port]" + Colors.UNDERLINE + " " + Colors.UNDERLINE + "[password]");
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.toLowerCase().startsWith("privmsg " + NICK + " :")) {
                    line = line.substring(("privmsg " + NICK + " :").length());
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2 && parts[0].toLowerCase().equals("connect")) {
                        String name = parts[1];
                        synchronized (servers) {
                            server = (ServerConnection) servers.get(name);
                        }
                        if (server != null) {
                            server.add(this);
                            replayHistory = true;
                            break;
                        } else {
                            sendRawLine(METHOD + "There is no server with the name " + Colors.BOLD + name);
                        }
                    } else if (parts.length == 2 && parts[0].toLowerCase().equals("remove")) {
                        String name = parts[1];
                        synchronized (bouncer) {
                            servers = bouncer.getServers();
                            server = (ServerConnection) servers.get(name);
                            if (server != null) {
                                if (server.getClientCount() == 0) {
                                    bouncer.remove(name);
                                } else {
                                    sendRawLine(METHOD + "Cannot remove " + Colors.BOLD + name + Colors.BOLD + ", as there are clients connected to the session.");
                                }
                            } else {
                                sendRawLine(METHOD + "Could not find server " + Colors.BOLD + name + Colors.BOLD + " to remove it.");
                            }
                        }
                    } else if (parts.length >= 3 && parts.length <= 5 && parts[0].toLowerCase().equals("create")) {
                        String name = parts[1];
                        String serverName = parts[2];
                        int port = 6667;
                        if (parts.length >= 4) {
                            try {
                                port = Integer.parseInt(parts[3]);
                            } catch (NumberFormatException e) {
                            }
                        }
                        String password = null;
                        if (parts.length >= 5) {
                            password = parts[4];
                        }
                        synchronized (servers) {
                            if (servers.containsKey(name)) {
                                sendRawLine(METHOD + "The name " + Colors.BOLD + name + Colors.BOLD + " is already used by another connection.");
                                continue;
                            }
                        }
                        server = new ServerConnection(serverName, port, password, nick, user);
                        server.add(this);
                        bouncer.add(name, server);
                        break;
                    } else {
                        sendRawLine(METHOD + "Unrecognised command.");
                    }
                }
            }
            if (line == null) {
                return;
            }
            server.sendToOtherClients(this, ":" + NICK + "!jbouncer@jbouncer PRIVMSG " + nick + " :" + nick + " connected to this session from " + socket.getInetAddress().getHostName());
            sendRawLine(":" + nick + "!jbouncer@jbouncer NICK :" + server.getNick());
            for (int i = 1; i < 5; i++) {
                String serverMessage = server.getServerMessages()[i];
                if (serverMessage != null) {
                    sendRawLine(serverMessage);
                }
            }
            if (!server.isConnected()) {
                sendRawLine(METHOD + "JBouncer is not currently connected to " + server.getServer() + ". JBouncer will keep trying to connect automatically every 5 minutes.");
            }
            String[] channels = server.getChannels();
            for (int i = 0; i < channels.length; i++) {
                String channel = channels[i];
                sendRawLine(":" + server.getNick() + "!jbouncer@jbouncer JOIN :" + channel);
                server.sendRawLine("NAMES " + channel);
                server.sendRawLine("TOPIC " + channel);
                server.sendRawLine("MODE " + channel);
            }
        } catch (IOException e) {
            return;
        }
        String line = null;
        try {
            if (replayHistory) {
                LinkedList history = server.getHistory();
                Iterator it = history.iterator();
                while (it.hasNext()) {
                    sendRawLine((String) it.next());
                }
            }
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("QUIT")) {
                    continue;
                }
                if (line.startsWith("PRIVMSG ") || line.startsWith("ACTION ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3 && parts[1].toLowerCase().equals(NICK.toLowerCase())) {
                        String nick = server.getNick();
                        String message = parts[2].substring(1).trim();
                        if (message.toLowerCase().equals("info")) {
                            sendPrivateMessage(nick, Colors.BOLD + "This Session" + Colors.BOLD);
                            sendPrivateMessage(nick, " Clients connected to this session: " + server.getClientCount());
                            sendPrivateMessage(nick, " This session is connected to " + server.getServer() + ":" + server.getPort());
                            sendPrivateMessage(nick, Colors.BOLD + "This JBouncer" + Colors.BOLD);
                            sendPrivateMessage(nick, " Registered users: " + manager.getBouncers().size());
                            sendPrivateMessage(nick, " Up since: " + new Date(manager.getStartTime()));
                            sendPrivateMessage(nick, " Server connections: " + bouncer.getServers().size());
                        }
                        continue;
                    }
                    String copiedLine = ":" + server.getNick() + "!jbouncer@jbouncer " + line;
                    server.addToHistory(copiedLine);
                    server.sendToOtherClients(this, copiedLine);
                }
                server.sendRawLine(line);
            }
        } catch (IOException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
        server.removeClient(this);
    }
