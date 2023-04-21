        public void run() {
            try {
                System.out.println("starting connection watchdog");
                Watchdog watchdog = new Watchdog(socket);
                watchdog.start();
                System.out.println("watchdog started.");
                in = new PushbackInputStream(watchdog.getInputStream());
                out = socket.getOutputStream();
                sendLine("OK " + serverGreeting);
                System.out.println("reading username");
                String username = readLineT();
                username = username.toLowerCase().trim();
                System.out.println("username is " + username);
                System.out.println("reading password");
                String password = readLineT();
                System.out.println("password is " + password);
                System.out.println("REMOVE THESE LINES ABOUT PASSWORD");
                System.out.println("reading real password");
                String realPassword = userAuthProperties.getProperty(username);
                System.out.println("real password is " + realPassword);
                if (!password.equals(realPassword)) {
                    System.out.println("failed auth");
                    Thread.sleep(8000);
                    out.write("FAIL incorrect username and/or password (for questions send email to intouch@trivergia.com)\r\n".getBytes("ASCII"));
                    out.flush();
                    socket.close();
                    return;
                } else {
                    Thread.sleep(3000);
                    System.out.println("succeeded auth");
                    out.write("OK please send your connection mode (command is the only value for now), NO REPLY will be sent, then start sending commands. you can use the help command for info.\r\n".getBytes("ASCII"));
                    out.flush();
                }
                this.username = username;
                System.out.println("waiting for mode");
                String mode = readLineT();
                System.out.println("mode was " + mode);
                if (!(mode.equalsIgnoreCase("command"))) throw new RuntimeException("invalid mode " + mode);
                if (mode.equalsIgnoreCase("command")) {
                    if (userConnections.get(username) != null) {
                        System.out.println("you are already connected");
                        out.write("FAIL You are already connected with a command socket\r\n".getBytes("ASCII"));
                        out.flush();
                        socket.close();
                        return;
                    }
                    userConnections.put(username, this);
                    notifyUserHere(username);
                    while (!socket.isClosed()) {
                        String line = readLineT();
                        System.out.println("line read from client user " + username + " was " + line.substring(0, Math.min(line.length(), 255)));
                        if (line == null) continue;
                        String command;
                        if (line.indexOf(" ") == -1) command = line; else command = line.substring(0, line.indexOf(" "));
                        String arguments = line.substring(command.length()).trim();
                        System.out.println("command was " + command);
                        if (command.equalsIgnoreCase("help")) {
                            sendTextGroup(command, "This server is used to allow multiple computers behind firewalls (in otherwords, not normally connectable to\r\n" + "each other) to exchange data. It was originally written as the server component of InTouch Communicator 3.\r\n" + "-------------------------------------\r\n" + "For more information, send an email to intouch@trivergia.com or webmaster@trivergia.com, or use the command commandhelp to list commands available or get info on a command.\r\n" + "You can also visit http://static.trivergia.com/intouch3-protocol for more information.\r\n" + "-------------------------------------\r\n" + "By convention, user metadata is made up of pipe-seperated strings (seperated by | ). each string has the format name=value. for\r\n" + "example, your metadata could be name1=value1|name2=value2|name3=value3 .\r\n" + "-------------------------------------\r\n" + "normal user accounts' usernames start with a letter or a number, and can contain letters, numbers, and spaces" + ". special accounts (i'll get to this in a bit) start with an underscore." + "accounts are not allowed to contain 2 underscores in a row.\r\n" + "Anyway, about special accounts. Special accounts are generally always online.\r\n" + "special accounts are usually there to serve useful purposes.\r\n" + "right now no special accounts have been implemented, but there are a few planned. the\r\n" + "special account _cache will be available for you to store large amounts of data" + "and later retrieve it. this could be used for sending offline messages.");
                        } else if (command.equalsIgnoreCase("listall")) {
                            StringBuilder responseBuilder = new StringBuilder();
                            for (Object user : userAuthProperties.keySet()) {
                                responseBuilder.append(user + "\r\n");
                            }
                            sendTextGroup(command, responseBuilder.toString());
                        } else if (command.equalsIgnoreCase("listonline")) {
                            StringBuilder responseBuilder = new StringBuilder();
                            for (Object user : userConnections.keySet()) {
                                responseBuilder.append(user + "\r\n");
                            }
                            sendTextGroup(command, responseBuilder.toString());
                        } else if (command.equalsIgnoreCase("listoffline")) {
                            StringBuilder responseBuilder = new StringBuilder();
                            ArrayList usersToSend = new ArrayList(userAuthProperties.keySet());
                            usersToSend.removeAll(userConnections.keySet());
                            for (Object user : usersToSend) {
                                responseBuilder.append(user + "\r\n");
                            }
                            sendTextGroup(command, responseBuilder.toString());
                        } else if (command.equalsIgnoreCase("sendmessage")) {
                            arguments = arguments.trim();
                            if (arguments.indexOf(" ") == -1) {
                                sendTextGroup(command, "FAIL invalid arguments supplied");
                            } else {
                                String to = arguments.substring(0, arguments.indexOf(" "));
                                String message = arguments.substring(arguments.indexOf(" ") + 1);
                                if (message.length() > 65535) {
                                    sendTextGroup(command, "FAIL message is too long (" + message.length() + " bytes), maximum length is 65535 bytes");
                                } else {
                                    if (userConnections.get(to) == null) {
                                        sendTextGroup(command, "FAIL the user specified is not connected right now (in the future, this command will support offline message caching)");
                                    } else {
                                        ConnectionHandler toHandler = userConnections.get(to);
                                        toHandler.sendTextGroup("RECEIVEMESSAGE", username + " " + message);
                                        sendTextGroup(command, "OK message successfully sent");
                                    }
                                }
                            }
                        } else if (command.equalsIgnoreCase("quit")) {
                            sendTextGroup(command, "OK disconnecting now");
                            break;
                        } else if (command.equalsIgnoreCase("getuserstatushash")) {
                            sendTextGroup(command, "" + userConnections.size() + "z" + userConnections.keySet().hashCode());
                        } else if (command.equalsIgnoreCase("commandhelp")) {
                            sendTextGroup(command, "current available commands are: COMMANDHELP, HELP, LISTALL, LISTONLINE, LISTOFFLINE, SENDMESSAGE, QUIT, GETTIME, GETUSERMETADATA, SETUSERMETADATA\r\n" + "if a message is sent to you, you will get an 'orphan' response (a response without a command to back it) where the\r\n" + "command is RECEIVEMESSAGE. to try this out, connect 2 users to the server, then use sendmessage to send a message from one to the other,\r\n" + "and see what you get on the receiving user's side.");
                        } else if (command.equalsIgnoreCase("gettime")) {
                            sendTextGroup(command, "" + System.currentTimeMillis());
                        } else if (command.equalsIgnoreCase("getusermetadata")) {
                            if (arguments.contains("/") || arguments.contains("\\") || arguments.contains(".")) throw new RuntimeException();
                            if (!new File(userMetadataFolder, arguments).exists()) {
                                sendTextGroup(command, "FAIL invalid user specified (the user either does not exist or does not have metadata set)");
                            } else {
                                sendTextGroup(command, "OK " + readFile(new File(userMetadataFolder, arguments)));
                            }
                        } else if (command.equalsIgnoreCase("getusermetadataattribute")) {
                            if (arguments.contains("/") || arguments.contains("\\")) throw new RuntimeException();
                            int sIndex = arguments.indexOf(" ");
                            String tUser = arguments.substring(0, sIndex);
                            String attribute = arguments.substring(sIndex + 1);
                            if (!new File(userMetadataFolder, tUser).exists()) {
                                sendTextGroup(command, "FAIL invalid user specified (the user either does not exist or does not have metadata set)");
                            } else {
                                String userMetadata = readFile(new File(userMetadataFolder, tUser));
                                Properties parsedMetadata = parseMetadata(userMetadata);
                                String result = parsedMetadata.getProperty(attribute);
                                if (result == null) sendTextGroup(command, "FAIL the attribute specified does not exist"); else sendTextGroup(command, "OK " + result);
                            }
                        } else if (command.equalsIgnoreCase("setusermetadata")) {
                            if (arguments.length() > (800 * 1024)) {
                                sendTextGroup(command, "FAIL user metadata too long");
                            }
                            writeFile(arguments, new File(userMetadataFolder, username));
                            sendTextGroup(command, "OK");
                        } else if (command.equalsIgnoreCase("setusermetadataproperty")) {
                            int pIndex = arguments.indexOf(" ");
                            String atName = arguments.substring(0, pIndex);
                            String atValue = arguments.substring(pIndex + 1);
                            Properties userMd = parseMetadata(readFile(new File(userMetadataFolder, username)));
                            userMd.setProperty(atName, atValue);
                            String newMd = generateMetadata(userMd);
                            if (newMd.length() > (800 * 1024)) {
                                sendTextGroup(command, "FAIL user metadata too long");
                            }
                            writeFile(newMd, new File(userMetadataFolder, username));
                            sendTextGroup(command, "OK");
                        } else if (command.equalsIgnoreCase("gettime")) {
                            sendTextGroup(command, "" + System.currentTimeMillis());
                        } else if (command.equalsIgnoreCase("NOP") || command.equalsIgnoreCase("NOOP")) {
                            sendTextGroup(command, "OK");
                        } else if (command.equalsIgnoreCase("CREATEWORKSPACE")) {
                            verifyNonPath(arguments);
                            vc(arguments);
                            verifyLength(arguments, 256);
                            long myWorkspaceCount = 0;
                            for (File f : workspaceFolder.listFiles()) {
                                Workspace workspace = (Workspace) readObjectFromFile(f);
                                if (workspace.getCreator().equalsIgnoreCase(username)) myWorkspaceCount++;
                            }
                            if (myWorkspaceCount > 20) throw new RuntimeException();
                            File workspaceFile = new File(workspaceFolder, arguments);
                            if (workspaceFile.exists()) sendTextGroup(command, "FAIL you already have a workspace with that id"); else {
                                Workspace workspace = new Workspace();
                                workspace.setCreator(username);
                                workspace.setId(arguments);
                                workspace.setUsers(new String[] {});
                                writeObjectToFile(workspace, workspaceFile);
                                new File(workspaceDataFolder, arguments).mkdirs();
                                sendTextGroup(command, "OK the workspace was successfully created");
                            }
                        } else if (command.equalsIgnoreCase("SETWORKSPACEPERMISSIONS")) {
                            verifyNonPath(arguments);
                            System.out.println("arguments:" + arguments);
                            String[] firstSplit = arguments.split(" ", 2);
                            String workspaceId = firstSplit[0];
                            String usersString = firstSplit[1];
                            System.out.println("workspaceid is " + workspaceId + " and usersString is " + usersString);
                            verifyLength(usersString, 5000);
                            String[] users = usersString.split(" ");
                            System.out.println(users.length + " users were included");
                            for (String u : users) System.out.println("u:" + u);
                            vc(workspaceId);
                            File workspaceFile = new File(workspaceFolder, workspaceId);
                            if (!workspaceFile.exists()) {
                                System.out.println("invalid workspace id");
                                sendTextGroup(command, "FAIL invalid workspace id");
                            } else {
                                System.out.println("ok, setting users");
                                Workspace workspace = (Workspace) readObjectFromFile(workspaceFile);
                                workspace.setUsers(users);
                                writeObjectToFile(workspace, workspaceFile);
                                sendTextGroup(command, "OK successfully set allowed users");
                            }
                        } else if (command.equalsIgnoreCase("DELETEWORKSPACE")) {
                            verifyNonPath(arguments);
                            vc(arguments);
                            recursiveDelete(new File(workspaceDataFolder, arguments));
                            recursiveDelete(new File(workspaceFolder, arguments));
                            sendTextGroup(command, "OK workspace successfully deleted");
                        } else if (command.equalsIgnoreCase("GETWORKSPACEPROPERTY")) {
                            verifyNonPath(arguments);
                            String[] firstSplit = arguments.split(" ", 2);
                            String workspaceId = firstSplit[0];
                            String key = firstSplit[1];
                            verifyIAmParticipant(workspaceId);
                            File workspaceDataFile = new File(workspaceDataFolder, workspaceId);
                            File keyFile = new File(workspaceDataFile, key);
                            if (!keyFile.exists()) {
                                sendTextGroup(command, "FAIL that key doesn't exist");
                            } else {
                                sendTextGroup(command, "OK " + readFile(keyFile));
                            }
                        } else if (command.equalsIgnoreCase("SETWORKSPACEPROPERTY")) {
                            String[] split = arguments.split(" ", 3);
                            String workspaceId = split[0];
                            String key = split[1];
                            String value = null;
                            if (split.length > 2) value = split[2];
                            verifyNonPath(workspaceId);
                            verifyNonPath(key);
                            verifyIAmParticipant(workspaceId);
                            verifyLength(key, 1024);
                            if (value != null) verifyLength(value, 50 * 1024);
                            File workspaceDataFile = new File(workspaceDataFolder, workspaceId);
                            long length = 0;
                            for (File f : workspaceDataFile.listFiles()) {
                                length += f.length();
                                length += f.getName().length();
                                length += 10;
                            }
                            if (length > (4 * 1000 * 1000)) {
                                throw new RuntimeException("workspace too large");
                            }
                            File keyFile = new File(workspaceDataFile, key);
                            if (value == null) keyFile.delete(); else writeFile(value, keyFile);
                            sendTextGroup(command, "OK property written/deleted successfully");
                        } else if (command.equalsIgnoreCase("LISTWORKSPACEPROPERTIES")) {
                            verifyNonPath(arguments);
                            String workspaceId = null;
                            String prefix = null;
                            arguments = arguments.trim();
                            if (arguments.indexOf(" ") == -1) {
                                workspaceId = arguments;
                            } else {
                                String[] split = arguments.split(" ", 2);
                                workspaceId = split[0];
                                prefix = split[1];
                            }
                            verifyIAmParticipant(workspaceId);
                            File workspaceDataFile = new File(workspaceDataFolder, workspaceId);
                            FilenameFilter filter = null;
                            final String fPrefix = prefix;
                            if (prefix != null) filter = new FilenameFilter() {

                                public boolean accept(File folder, String name) {
                                    return name.startsWith(fPrefix);
                                }
                            };
                            String[] keys = workspaceDataFile.list(filter);
                            sendTextGroup(command, "OK " + delimited(keys, "\n"));
                        } else if (command.equalsIgnoreCase("LISTOWNEDWORKSPACES")) {
                            sendTextGroup(command, "");
                        } else if (command.equalsIgnoreCase("LISTWORKSPACES")) {
                            sendTextGroup(command, "");
                        } else if (command.equalsIgnoreCase("CANACCESS")) {
                            verifyNonPath(arguments);
                            try {
                                verifyIAmParticipant(arguments);
                                sendTextGroup(command, "OK you have access to this workspace");
                            } catch (Exception e) {
                                sendTextGroup(command, "FAIL you do not have access to this workspace");
                            }
                        } else if (command.equalsIgnoreCase("")) {
                            sendTextGroup(command, "");
                        } else if (command.equalsIgnoreCase("")) {
                            sendTextGroup(command, "");
                        } else if (command.equalsIgnoreCase("")) {
                            sendTextGroup(command, "");
                        } else if (command.equalsIgnoreCase("")) {
                            sendTextGroup(command, "");
                        } else if (command.equalsIgnoreCase("")) {
                            sendTextGroup(command, "");
                        } else {
                            sendTextGroup(command, "FAIL unknown command");
                        }
                        out.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (this.username != null && userConnections.get(username) == this) {
                            userConnections.remove(username);
                            try {
                                notifyUserGone(username);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    } finally {
                        System.out.println("before subtracting from numconnections, it is " + numConnections);
                        numConnections--;
                    }
                }
            }
        }
