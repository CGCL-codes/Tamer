    public pnlClientGUI() {
        initComponents();
        if (!java.beans.Beans.isDesignTime()) {
            pnlInput.setDirectionShow(false);
            pnlInput.initList(AppGlobals.getInstance().getAppSettingsDirectory() + "list.client.txt");
            pnlReceivedData.setType(ViewersPanel.Type.input);
            pnlReceivedData.setTitle("Received data");
            pnlSentData.setType(ViewersPanel.Type.output);
            pnlSentData.setTitle("Sent data");
            pnlSystemLog.setTitle("System log");
            cmdDisconnect.setSelected(true);
            Globals.getInstance().getDlgOptions().addListener(new SettingsListenerIF() {

                public void ServerFileListDeleted() {
                }

                public void ClientFileListDeleted() {
                    pnlInput.refreshList();
                }

                public void ServerSentWindowDataBufferSize(int size) {
                }

                public void ServerReceivedWindowDataBufferSize(int size) {
                }

                public void ClientSentWindowDataBufferSize(int size) {
                    pnlSentData.setMaxKBytes(size);
                }

                public void ClientReceivedWindowDataBufferSize(int size) {
                    pnlReceivedData.setMaxKBytes(size);
                }

                public void SendingDataChunkSize(int size) {
                    pnlInput.setPacketSize(size);
                }

                public void ServerLoggingSettingsChanged(String path, int size, int filesCount, boolean isLoggingEnabled) {
                }

                public void ClientLoggingSettingsChanged(String path, int size, int filesCount, boolean isLoggingEnabled) {
                    sl = new StreamLogger(path, "client.log", size, filesCount);
                    sl.setEnableLogging(isLoggingEnabled);
                }
            });
            oneSecondTimer = new javax.swing.Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireClientUpTimeTicker();
                }
            });
            Globals.getInstance().getClient().addListener(new ClientEventsListenerIF() {

                public void DataArrived(ChannelHandlerContext ctx, final MessageEvent e) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            outputDataArrived((BigEndianHeapChannelBuffer) e.getMessage());
                        }
                    });
                }

                public void Connected(ChannelHandlerContext ctx, ChannelStateEvent e) {
                    clientPort = Integer.parseInt(pnlServerPort.getPort());
                    clientAddress = pnlServerAddress.getIPAddress();
                    oneSecondTimer.start();
                    pnlSystemLog.addLog("Connected to server: " + clientAddress + ":" + clientPort, new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldGreen));
                    lockFields(true);
                    pnlInput.lockButtons(false);
                    fireClientConnect();
                    connected = true;
                }

                public void Disconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
                    connected = false;
                    Globals.getInstance().getClient().shutdown();
                    oneSecondTimer.stop();
                    pnlSystemLog.addLog("Disconnected.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldOrange));
                    lockFields(false);
                    pnlInput.lockButtons(true);
                    cmdDisconnect.setSelected(true);
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            if (Globals.getConfig().getProperty("Client.clear_prompt").toString().equalsIgnoreCase("true")) {
                                int ret = JOptionPane.showConfirmDialog(null, "Would you like to clear CLIENT output windows?", "Confirmation", JOptionPane.YES_NO_OPTION);
                                if (ret == 0) {
                                    pnlSystemLog.clearAll();
                                    pnlReceivedData.clearAll();
                                    pnlSentData.clearAll();
                                }
                            }
                        }
                    });
                    fireClientDisconnect();
                }

                public void ExceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
                    pnlSystemLog.addLog("Error: " + e.getCause().getMessage(), new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error, e.getChannel().getId().toString()));
                }

                public void DataSentFromQueue(final ChannelBuffer bufferedMessage) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            outputDataSent(bufferedMessage);
                        }
                    });
                }
            });
            pnlInput.addEventsListener(new InputPanelListenerIF() {

                public void SendChannelBuffer(ChannelBuffer bufferedMessage) {
                    if (pnlInput.isAddCR()) {
                        bufferedMessage.writeByte((byte) 0xd);
                    }
                    if (pnlInput.isAddLF()) {
                        bufferedMessage.writeByte((byte) 0xa);
                    }
                    sendDataFromClientOut(bufferedMessage);
                }

                public void ExceptionCaught(String msg) {
                    pnlSystemLog.addLog(msg, new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
                }
            });
            pnlInput.lockButtons(true);
            Globals.getInstance().getDlgOptions().fireAll();
        }
    }
