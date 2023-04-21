    public void listen() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(6789);
            while (true) {
                String clientData;
                String clientDataArray[];
                String clientCommand;
                String clientCommandParameters[];
                connectionSocket = welcomeSocket.accept();
                InputStreamReader inFromClientStream = new InputStreamReader(connectionSocket.getInputStream());
                BufferedReader inFromClient = new BufferedReader(inFromClientStream);
                clientData = inFromClient.readLine();
                clientDataArray = clientData.split(":");
                clientCommand = clientDataArray[0];
                clientCommandParameters = new String[clientDataArray.length - 1];
                System.arraycopy(clientDataArray, 1, clientCommandParameters, 0, clientDataArray.length - 1);
                if (clientCommand != null) {
                    String reportClientData = ">> Received command \"" + clientCommand + "\" from IP " + connectionSocket.getInetAddress();
                    if (clientCommandParameters.length != 0) {
                        reportClientData += " with parameters ";
                        for (int i = 0; i < clientCommandParameters.length; i++) {
                            if (i != 0) {
                                reportClientData += ", ";
                            }
                            reportClientData += "'" + clientCommandParameters[i] + "'";
                        }
                    }
                    gui.writeLine(reportClientData);
                }
                System.out.println(clientCommand);
                if (clientCommand.equals("hello") || clientCommand.equals("handshake")) {
                    commandHandshake();
                }
                if (clientCommand.equals("login")) {
                    commandLogin(clientCommandParameters);
                }
                if (clientCommand.equals("executeQuerySelect") && clientCommandParameters.length > 0) {
                    commandExecuteQuerySelect(clientCommandParameters[0], Boolean.parseBoolean(clientCommandParameters[1]));
                }
                if (clientCommand.equals("executeQuery") && clientCommandParameters.length > 0) {
                    commandExecuteQuery(clientCommandParameters[0]);
                }
                if (clientCommand.equals("createBackup")) {
                    createBackup();
                }
                if (clientCommand.equals("restoreBackup") && clientCommandParameters.length == 0) {
                    commandRestoreBackup();
                }
                if (clientCommand.equals("restoreBackup") && clientCommandParameters.length == 1) {
                    commandRestoreBackup(clientCommandParameters[0]);
                }
                if (clientCommand.equals("sendLog")) {
                    commandSendLog();
                }
                inFromClient.close();
                connectionSocket.close();
            }
        } catch (BindException ex) {
            gui.writeLine("ERROR: Failed to initialize connection socket (address already in use).");
            ex.printStackTrace();
        } catch (Exception ex) {
            gui.writeLine("ERROR: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
