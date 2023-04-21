    public void packetRead(SocketThread socketThread, Packet packet) {
        super.packetRead(socketThread, packet);
        User user = findUser(socketThread);
        if (user != null) {
            if (packet.getCommand().equals(PING)) {
                long time = packet.getTime();
                sendReply(socketThread, SUCCESS, PING, new Long(time));
            } else if (packet.getCommand().equals(GET)) {
                String userName = packet.getString(USER, null);
                String[] userNames = packet.get(String[].class, USERS, null);
                String hostName = packet.getString(HOST, null);
                String[] variables = packet.get(String[].class, VARIABLES, null);
                if (variables != null) {
                    if (userName != null) {
                        User getUser = findUser(userName);
                        if (getUser != null) {
                            Attributes attributes = getUser.getAttributes();
                            if (attributes != null) {
                                Attributes resultAttributes = new Attributes();
                                for (int j = 0; j < variables.length; ++j) {
                                    if (isValidUserGetVariable(variables[j])) resultAttributes.put(variables[j], attributes.get(variables[j]));
                                }
                                sendReply(socketThread, SUCCESS, userName, resultAttributes);
                            } else {
                                sendFailure(socketThread, packet, "No Attributes found for given " + USER);
                            }
                        } else {
                            sendFailure(socketThread, packet, "Invalid " + USER + ": " + userName);
                        }
                    } else if (hostName != null) {
                        Host host = findHost(hostName);
                        if (host != null) {
                            Attributes attributes = host.getAttributes();
                            if (attributes != null) {
                                Attributes resultAttributes = new Attributes();
                                for (int j = 0; j < variables.length; ++j) {
                                    if (isValidHostGetVariable(variables[j])) resultAttributes.put(variables[j], attributes.get(variables[j]));
                                }
                                hostUpdated(host);
                                sendReply(socketThread, SUCCESS, hostName, resultAttributes);
                            } else {
                                sendFailure(socketThread, packet, "No Attributes found for given " + HOST);
                            }
                        } else {
                            sendFailure(socketThread, packet, "Invalid " + HOST + ": " + userName);
                        }
                    } else if (userNames != null) {
                        ArrayList<String> userList = new ArrayList<String>();
                        ArrayList<Attributes> attributesList = new ArrayList<Attributes>();
                        for (int i = 0; i < userNames.length; ++i) {
                            User getUser = findUser(userNames[i]);
                            if (getUser != null) {
                                Attributes attributes = getUser.getAttributes();
                                if (attributes != null) {
                                    userList.add(userNames[i]);
                                    Attributes resultAttributes = new Attributes();
                                    attributesList.add(resultAttributes);
                                    for (int j = 0; j < variables.length; ++j) {
                                        if (isValidUserGetVariable(variables[j])) resultAttributes.put(variables[j], attributes.get(variables[j]));
                                    }
                                }
                            }
                        }
                        if (attributesList.size() > 0) sendReply(socketThread, SUCCESS, userList.toArray(new String[userList.size()]), attributesList.toArray(new Attributes[attributesList.size()])); else sendFailure(socketThread, packet, "No Attributes found for given " + USERS);
                    } else {
                        Attributes attributes = getAttributes();
                        Attributes resultAttributes = new Attributes();
                        for (int i = 0; i < variables.length; ++i) {
                            if (isValidGetVariable(variables[i])) {
                                if (variables[i].equals(USERS)) {
                                    resultAttributes.put(USERS, getUsers());
                                } else if (variables[i].equals(HOSTS)) {
                                    resultAttributes.put(HOSTS, getHosts());
                                } else if (variables[i].equals(CHANNELS)) {
                                    resultAttributes.put(CHANNELS, getChannels());
                                } else {
                                    resultAttributes.put(variables[i], attributes.get(variables[i]));
                                }
                            }
                        }
                        sendReply(socketThread, SUCCESS, getLobbyName(), resultAttributes);
                    }
                } else {
                    sendFailure(socketThread, packet, "No " + VARIABLES + " requested");
                }
            } else if (packet.getCommand().equals(SET)) {
                String[] variables = packet.get(String[].class, VARIABLES, null);
                Object[] values = packet.get(Object[].class, VALUES, null);
                if (variables != null) {
                    sendFailure(socketThread, packet, "No " + VARIABLES + " given");
                } else if (values != null) {
                    sendFailure(socketThread, packet, "No " + VALUES + " given");
                } else {
                    Attributes attributes = getAttributes(socketThread);
                    if (attributes != null) {
                        ArrayList<String> invalid = new ArrayList<String>();
                        for (int i = 0; i < variables.length && i < values.length; ++i) {
                            if (isValidUserSetVariable(variables[i])) attributes.put(variables[i], values[i]); else invalid.add(variables[i]);
                        }
                        if (invalid.size() == 0) sendSuccess(socketThread, packet); else {
                            StringBuilder invalids = new StringBuilder();
                            for (Iterator<String> itr = invalid.iterator(); itr.hasNext(); ) invalids.append(itr.next() + ", ");
                            sendFailure(socketThread, packet, "Invalid " + VARIABLES + " given: " + invalids);
                        }
                    } else {
                        sendFailure(socketThread, packet, "No Attributes found for User");
                    }
                }
            } else if (packet.getCommand().equals(MESSAGE)) {
                String channelName = packet.getString(CHANNEL, null);
                String message = packet.getString(MESSAGE, null);
                Channel channel = null;
                if (channelName != null) {
                    channel = findChannel(channelName);
                    if (channel != null) {
                        redirectMessage(user.getUsername(), channel, message);
                    } else {
                        sendFailure(socketThread, packet, "Invalid " + CHANNEL);
                    }
                } else {
                    redirectMessage(user.getUsername(), null, message);
                }
            } else if (packet.getCommand().equals(CREATE_HOST)) {
                String hostName = packet.getString(NAME, null);
                String portName = packet.getString(PORT, null);
                if (hostName == null) {
                    sendFailure(socketThread, packet, "No " + NAME + " given");
                } else if (portName == null) {
                    sendFailure(socketThread, packet, "No " + PORT + " given");
                } else if (hosts.contains(user)) {
                    sendFailure(socketThread, packet, "Host already active for current User");
                } else {
                    try {
                        int port = Integer.parseInt(portName);
                        InetSocketAddress address = new InetSocketAddress(socketThread.getSocket().getInetAddress(), port);
                        Host host = new Host(hostName, user.getUsername(), address);
                        synchronized (this) {
                            hosts.put(user, host);
                        }
                        sendSuccess(socketThread, packet);
                        hostCreated(host);
                    } catch (NumberFormatException e) {
                        sendFailure(socketThread, packet, "Invalid " + PORT);
                    }
                }
            } else if (packet.getCommand().equals(CANCEL_HOST)) {
                Host host = findHost(user);
                if (host != null) {
                    synchronized (this) {
                        hosts.remove(user);
                    }
                    sendSuccess(socketThread, packet);
                    hostCanceled(host);
                } else {
                    sendFailure(socketThread, packet, "No Host active for current User");
                }
            } else if (packet.getCommand().equals(CREATE_CHANNEL)) {
                String channelName = packet.getString(NAME, null);
                if (channelName != null) {
                    Channel channel = findChannel(channelName);
                    if (channel == null) {
                        channel = new Channel(channelName);
                        channel.addUser(user.getUsername());
                        synchronized (this) {
                            channels.put(channelName, channel);
                        }
                        sendSuccess(socketThread, packet);
                        channelCreated(channel);
                    } else {
                        sendFailure(socketThread, packet, NAME + " already exists: " + channelName);
                    }
                } else {
                    sendFailure(socketThread, packet, "No " + NAME + " given");
                }
            } else if (packet.getCommand().equals(JOIN_CHANNEL)) {
                String channelName = packet.getString(NAME, null);
                if (channelName != null) {
                    Channel channel = findChannel(channelName);
                    if (channel != null) {
                        channel.addUser(user.getUsername());
                        sendSuccess(socketThread, packet);
                        channelJoined(user, channel);
                    } else {
                        sendFailure(socketThread, packet, "Invalid " + NAME + ": " + channelName);
                    }
                } else {
                    sendFailure(socketThread, packet, "No " + NAME + " given");
                }
            } else if (packet.getCommand().equals(LEAVE_CHANNEL)) {
                String channelName = packet.getString(NAME, null);
                if (channelName != null) {
                    Channel channel = findChannel(channelName);
                    if (channel != null) {
                        channel.removeUser(user.getUsername());
                        sendSuccess(socketThread, packet);
                        channelLeft(user, channel);
                        if (channel.getUserCount() == 0) {
                            synchronized (this) {
                                channels.remove(channel);
                            }
                            channelRemoved(channel);
                        }
                    } else {
                        sendFailure(socketThread, packet, "Invalid " + NAME + ": " + channelName);
                    }
                } else {
                    sendFailure(socketThread, packet, "No " + NAME + " given");
                }
            }
        } else {
            sendFailure(socketThread, packet, "Not signed in");
        }
    }
