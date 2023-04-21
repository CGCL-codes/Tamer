    public void connectToServer() {
        ConnectDialog dialog = new ConnectDialog(rootWindow);
        ConnectionInfo connectionInfo = new ConnectionInfo();
        AuthToken token = new AuthToken();
        int retCode;
        rootWindow.disableConnectControls();
        dialog.setVisible(true);
        connectionInfo.setServerHostname("chat.chat.ru");
        connectionInfo.setServerPort(80);
        connectionInfo.setUsername(dialog.getLogin());
        connectionInfo.setPassword(dialog.getPassword());
        token.setLogin(dialog.getLogin());
        token.setPassword(dialog.getPassword());
        try {
            protocolHandler = core.getConnectedProtocolHandler(connectionInfo);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        retCode = -100;
        try {
            retCode = protocolHandler.authenticate(token);
        } catch (NetworkException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        if (retCode == ProtocolHandlerInterface.AUTH_OK) {
            SelectChannelDialog channelDialog;
            ConnectedChannelInterface channelInterface;
            channelDialog = new SelectChannelDialog(rootWindow);
            channelDialog.setVisible(true);
            try {
                JTextField messageField;
                JButton sendButton;
                JComboBox emotionComboBox;
                ChannelPane channelPane;
                ChannelViewContainer container;
                ChannelController controller;
                channelInterface = protocolHandler.joinChannel(channelDialog.getChannelName());
                container = new ChannelViewContainer(channelDialog.getChannelName());
                controller = new ChannelController(channelInterface);
                channelPane = new ChannelPane();
                messageField = new JTextField();
                sendButton = new JButton("�������");
                emotionComboBox = new JComboBox(new String[] { "������� �����", "���������� �������", "������", "���������", "�����", "��������", "��������" });
                controller.setChannelPane(channelPane);
                controller.setMessageInputField(messageField);
                controller.setEmotionComboBox(emotionComboBox);
                channelPane.setChannelController(controller);
                sendButton.addActionListener(controller.getSendMessageActionListener());
                container.setChannelPane(channelPane);
                container.setMessageInputField(messageField);
                container.setSendButton(sendButton);
                container.setTopPanel(new JPanel());
                container.setUsersPanel(new JPanel());
                container.setEmotionComboBox(emotionComboBox);
                rootWindow.setVisible(false);
                rootWindow.addChannelViewContainer(container);
                rootWindow.setVisible(true);
                controller.startMessageReadingThread();
            } catch (NotAuthenticatedException e) {
                e.printStackTrace();
            } catch (NetworkException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (BadTicketException e) {
                e.printStackTrace();
            }
        }
    }
