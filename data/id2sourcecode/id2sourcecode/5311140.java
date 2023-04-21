    private void hostServer(StringTokenizer tokens) {
        if (tokens.countTokens() >= 4) {
            String capStr = tokens.nextToken();
            String portStr = tokens.nextToken();
            if (isInteger(capStr) && isInteger(portStr)) {
                int cap = Integer.parseInt(capStr);
                int port = Integer.parseInt(portStr);
                if (portFree(port, false)) {
                    String serverName = tokens.nextToken();
                    if (serverName.charAt(0) == '\'') {
                        while (tokens.hasMoreTokens() && !serverName.endsWith("'")) {
                            serverName = serverName.concat(" ".concat(tokens.nextToken()));
                        }
                    }
                    serverName = serverName.replace("'", "");
                    if (tokens.hasMoreTokens()) {
                        String advPath = tokens.nextToken();
                        if (advPath.charAt(0) == '\'') {
                            while (tokens.hasMoreTokens() && !advPath.endsWith("'")) {
                                advPath = advPath.concat(" ".concat(tokens.nextToken()));
                            }
                        }
                        advPath = advPath.replace("'", "");
                        server = new NetworkServerImp(terminal, netprefs, help, version);
                        if (server.hostServer(serverName, advPath, cap, port)) {
                            server.start();
                            hostingServer = true;
                        } else {
                            terminal.writeTo("\nServerhost failed. Invalid adventure file or port already in use.");
                        }
                    } else {
                        terminal.writeTo("\nYou must specify a maximum capacity, port and server name to host a server.\n" + "Syntax: serverhost 24 723 'Sample Server Name' 'C:/file adddress/'");
                    }
                } else {
                    terminal.writeTo("\nERROR: Port " + port + " is already in use or reserved for channels. Cannot host server.");
                }
            } else {
                terminal.writeTo("\nYou must specify a maximum capacity, port and server name to host a server.\n" + "Syntax: serverhost 24 723 'Sample Server Name' 'C:/file adddress/'");
            }
        } else {
            terminal.writeTo("\nYou must specify a maximum capacity, port and server name to host a server.\n" + "Syntax: serverhost 24 723 'Sample Server Name' 'C:/file adddress/'");
        }
    }
