    public void run() {
        while (connected) {
            try {
                int code = rd.read();
                if (code == 1001) {
                    try {
                        int startX = Integer.valueOf(rd.readLine());
                        int startY = Integer.valueOf(rd.readLine());
                        int stopX = Integer.valueOf(rd.readLine());
                        int stopY = Integer.valueOf(rd.readLine());
                        int userID = rd.read();
                        User user = userHandler.getUser(userID);
                        ((ClientDrawspace) user.getWorkspaceInUse()).drawLine(startX, startY, stopX, stopY, user);
                    } catch (NullPointerException e) {
                        System.err.println("Client: 1001: NullpointerException : \n" + "The Workspace does NOT exist or is NOT a Drawspace");
                    }
                } else if (code == 1002) {
                    int wsID = rd.read();
                    ((ClientDrawspace) wsHandler.getWorkspace(wsID)).clearImage();
                } else if (code == 1004) {
                    try {
                        int startX = Integer.valueOf(rd.readLine());
                        int startY = Integer.valueOf(rd.readLine());
                        int stopX = Integer.valueOf(rd.readLine());
                        int stopY = Integer.valueOf(rd.readLine());
                        int userID = rd.read();
                        User user = userHandler.getUser(userID);
                        ((ClientDrawspace) user.getWorkspaceInUse()).drawBox(startX, startY, stopX, stopY, user);
                    } catch (NullPointerException e) {
                        System.err.println("Client: 1004: NullpointerException : \n" + "The Workspace does NOT exist or is NOT a Drawspace");
                    }
                } else if (code == 1006) {
                    try {
                        int startX = Integer.valueOf(rd.readLine());
                        int startY = Integer.valueOf(rd.readLine());
                        int stopX = Integer.valueOf(rd.readLine());
                        int stopY = Integer.valueOf(rd.readLine());
                        int userID = rd.read();
                        User user = userHandler.getUser(userID);
                        ((ClientDrawspace) user.getWorkspaceInUse()).drawEllipse(startX, startY, stopX, stopY, user);
                    } catch (NullPointerException e) {
                        System.err.println("Client: 1006: NullpointerException : \n" + "The Workspace does NOT exist or is NOT a Drawspace");
                    }
                } else if (code == 1008) {
                    try {
                        int startX = Integer.valueOf(rd.readLine());
                        int startY = Integer.valueOf(rd.readLine());
                        String text = rd.readLine();
                        int userID = rd.read();
                        User user = userHandler.getUser(userID);
                        ((ClientDrawspace) user.getWorkspaceInUse()).drawText(startX, startY, text, user);
                    } catch (NullPointerException e) {
                        System.err.println("Client: 1008: NullpointerException : \n" + "The Workspace does NOT exist or is NOT a Drawspace");
                    }
                } else if (code == 1010) {
                    try {
                        int startX = Integer.valueOf(rd.readLine());
                        int startY = Integer.valueOf(rd.readLine());
                        int stopX = Integer.valueOf(rd.readLine());
                        int stopY = Integer.valueOf(rd.readLine());
                        int userID = rd.read();
                        User user = userHandler.getUser(userID);
                        ((ClientDrawspace) user.getWorkspaceInUse()).drawClearArea(startX, startY, stopX, stopY);
                    } catch (NullPointerException e) {
                        System.err.println("Client: 1010: NullpointerException : \n" + "The Workspace does NOT exist or is NOT a Drawspace");
                    }
                } else if (code == 1012) {
                    int wsID = rd.read();
                    long imageBytes = Long.valueOf(rd.readLine());
                    ProgressMonitor progressMonitor = new ProgressMonitor(client.getMainGui(), "Receiving image from server", "", 0, (int) imageBytes);
                    progressMonitor.setMillisToDecideToPopup(50);
                    progressMonitor.setMillisToPopup(250);
                    ClientDrawspace ds = (ClientDrawspace) wsHandler.getWorkspace(wsID);
                    int imagePrefix = 0;
                    String tmpFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ds.getWorkspaceName() + imagePrefix + ".jpg";
                    File tmpImage = new File(tmpFile);
                    while (tmpImage.exists()) {
                        tmpFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ds.getWorkspaceName() + imagePrefix + ".jpg";
                        imagePrefix++;
                        tmpImage = new File(tmpFile);
                    }
                    FileOutputStream imageOut = new FileOutputStream(tmpImage);
                    int receivedBytes = 0;
                    for (long i = 0; i < imageBytes; i++) {
                        imageOut.write(rd.read());
                        receivedBytes++;
                        progressMonitor.setProgress(receivedBytes);
                    }
                    imageOut.close();
                    ds.setImage(tmpImage);
                    tmpImage.delete();
                    progressMonitor.close();
                } else if (code == 5) {
                    int fromID = rd.read();
                    String message = rd.readLine();
                    boolean privateMessage = Boolean.valueOf(rd.readLine());
                    String name = userHandler.getUser(fromID).getUserName();
                    String chatMessage;
                    if (privateMessage) {
                        chatMessage = "<" + name + " private> " + message;
                    } else {
                        chatMessage = "<" + name + "> " + message;
                    }
                    client.getMainGui().showChatMessage(chatMessage);
                } else if (code == 6) {
                } else if (code == -1 || code == 7) {
                    client.getMainGui().showMessageInPopUp("The server has disconnected");
                    client.getMainGui().disconnect();
                } else if (code == 8) {
                    client.getMainGui().setProgressStatus(7);
                } else if (code == 101) {
                    int sizeX = rd.read();
                    int sizeY = rd.read();
                    String wsName = rd.readLine();
                    int ownerID = rd.read();
                    int wsID = rd.read();
                    boolean locked = Boolean.valueOf(rd.readLine());
                    boolean onlyUser = Boolean.valueOf(rd.readLine());
                    int backgroundColor = Integer.valueOf(rd.readLine());
                    int lockedByClient = Integer.valueOf(rd.read());
                    ClientDrawspace x = new ClientDrawspace(client, sizeX, sizeY, wsName, ownerID, wsID, locked, onlyUser, lockedByClient, backgroundColor);
                    wsHandler.addWorspace(x);
                    client.updateWorkspaceTable();
                } else if (code == 102) {
                    int clientID = rd.read();
                    String userName = rd.readLine();
                    User newUser = new User(userName, clientID);
                    if (clientID == client.getClientID()) {
                        client.setUser(newUser);
                    }
                    userHandler.addClient(newUser);
                    client.updateUserTable();
                } else if (code == 103) {
                    int removeID = rd.read();
                    userHandler.removeClient(userHandler.getUser(removeID));
                    client.updateUserTable();
                } else if (code == 104) {
                    int workspaceID = rd.read();
                    client.getMainGui().removeWorkspace(wsHandler.getWorkspace(workspaceID));
                    wsHandler.removeWorkspace(wsHandler.getWorkspace(workspaceID), client.getClientID());
                    client.updateWorkspaceTable();
                } else if (code == 301) {
                    int userID = rd.read();
                    String newName = rd.readLine();
                    userHandler.getUser(userID).setUserName(newName);
                    client.updateUserTable();
                } else if (code == 303) {
                    boolean status = Boolean.valueOf(rd.readLine());
                    client.getUser().setAdmin(status);
                    client.getMainGui().changeAdminStatus(true);
                } else if (code == 310) {
                    int workID = rd.read();
                    String newWsName = rd.readLine();
                    wsHandler.getWorkspace(workID).setWorkspaceName(newWsName);
                    client.updateWorkspaceTable();
                } else if (code == 312) {
                    int wsID = rd.read();
                    boolean lockedStatus = Boolean.valueOf(rd.readLine());
                    int lockedBy = rd.read();
                    wsHandler.getWorkspace(wsID).setLocked(lockedStatus, lockedBy);
                    client.updateWorkspaceTable();
                } else if (code == 313) {
                    int worID = rd.read();
                    boolean onlyOwner = Boolean.valueOf(rd.readLine());
                    wsHandler.getWorkspace(worID).setOnlyOwnerCanWrite(onlyOwner);
                    client.updateWorkspaceTable();
                } else if (code == 314) {
                    int workspacID = rd.read();
                    int owner = rd.read();
                    wsHandler.getWorkspace(workspacID).setOwner(owner);
                    client.updateWorkspaceTable();
                } else if (code == 316) {
                    int wsID = rd.read();
                    int userID = rd.read();
                    userHandler.getUser(userID).setWorkspaceIDInUse(wsID);
                    userHandler.getUser(userID).setWorkspaceIDInUse(wsHandler.getWorkspace(wsID));
                } else if (code == 318) {
                    int thickness = rd.read();
                    int userID = rd.read();
                    userHandler.getUser(userID).setStrokeInUse(thickness);
                } else if (code == 320) {
                    boolean enableAnti = Boolean.valueOf(rd.readLine());
                    int userID = rd.read();
                    userHandler.getUser(userID).setAntialiasingEnabled(enableAnti);
                } else if (code == 321) {
                    int wsID = rd.read();
                    int bgColor = Integer.valueOf(rd.readLine());
                    if (wsHandler.getWorkspace(wsID) instanceof ClientDrawspace) {
                        ((ClientDrawspace) (wsHandler.getWorkspace(wsID))).setBackgroundColorRGB(bgColor);
                        client.updateWorkspaceTable();
                    } else {
                        System.out.println("Can't change the background color, because the to" + "to change Workspace is not a Drawspace");
                    }
                } else if (code == 323) {
                    int colorUse = Integer.valueOf(rd.readLine());
                    int userID = rd.read();
                    userHandler.getUser(userID).setColorInUseRGB(colorUse);
                } else if (code == 325) {
                    boolean filledStatus = Boolean.valueOf(rd.readLine());
                    int userID = rd.read();
                    userHandler.getUser(userID).setFilled(filledStatus);
                } else if (code == 5002) {
                    client.getMainGui().setAdminPasswordStatus(5002);
                } else if (code == 5004) {
                    client.getMainGui().setConnectPasswordStatus(5004);
                } else if (code == 10003) {
                    client.getMainGui().showMessageInPopUp("Message from Server: Can't change the user name," + " because the name is already used by another user");
                    break;
                } else if (code == 10004) {
                    client.getMainGui().showMessageInPopUp("Message from Server: The workspace name is already in use");
                    break;
                } else if (code == 10005) {
                    client.getMainGui().showMessageInPopUp("Message from Server: No rights to change the lock status");
                    break;
                } else if (code == 10006) {
                    client.getMainGui().showMessageInPopUp("Message from Server: no rights to change " + "the only owner can write status");
                } else if (code == 10007) {
                    client.getMainGui().showMessageInPopUp("Message from Server: " + "Can not remove the workspace, because the workspace is locked");
                } else if (code == 10008) {
                    client.getMainGui().showMessageInPopUp("Message from Server: " + "no permission to remove workspace");
                } else if (code == 10010 || code == 10011 || code == 10012 || code == 10013) {
                    client.getMainGui().setConnectPasswordStatus(code);
                    client.getMainGui().setAdminPasswordStatus(code);
                } else if (code == 10014) {
                    client.getMainGui().showMessageInPopUp("The server has no free memory to create new workspaces.\n" + "Please contact your adminstrator to start NetDaggle-Server " + "with more memory or delete workspaces on the server");
                } else {
                    System.out.println("Client: Unkown code " + code + " ... continue working");
                }
            } catch (UTFDataFormatException f) {
                f.printStackTrace();
            } catch (SocketTimeoutException e) {
                System.err.println("Client: Lost connection to the server : Timed out");
                client.getMainGui().showMessageInPopUp("Lost connection to the server : Timed out");
                client.getMainGui().disconnect();
            } catch (IOException e) {
                System.out.println("Client: The stream is closed");
                client.getMainGui().disconnect();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
