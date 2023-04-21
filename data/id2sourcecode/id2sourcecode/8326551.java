    protected void showUpdateBotDialog(final IrcBot bot) {
        VerticalPanel panel = new VerticalPanel();
        final PopupPanel box = new PopupPanel(false, true);
        box.setWidget(panel);
        boolean isAdding = bot.getBotid() == -1;
        panel.add(new Header3(isAdding ? "Add a new IRC bot" : "Editing an IRC bot"));
        FlexTable table = new FlexTable();
        panel.add(table);
        FlexCellFormatter format = table.getFlexCellFormatter();
        final TextBox nickField = new TextBox();
        final TextBox serverField = new TextBox();
        final TextBox portField = new TextBox();
        final PasswordTextBox passwordField = new PasswordTextBox();
        final TextBox channelField = new TextBox();
        nickField.setText(bot.getNick());
        serverField.setText(bot.getServer());
        portField.setText("" + bot.getPort());
        passwordField.setText(bot.getPassword());
        channelField.setText(bot.getChannel());
        table.setText(0, 0, "Nick");
        table.setText(1, 0, "Server");
        table.setText(2, 0, "Port");
        table.setText(3, 0, "Password");
        table.setText(4, 0, "Channel");
        table.setWidget(0, 1, nickField);
        table.setWidget(1, 1, serverField);
        table.setWidget(2, 1, portField);
        table.setWidget(3, 1, passwordField);
        table.setWidget(4, 1, channelField);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button saveButton = new Button(isAdding ? "Create" : "Save");
        Button cancelButton = new Button("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        table.setWidget(5, 1, buttonPanel);
        box.center();
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int portNumber;
                try {
                    portNumber = Integer.parseInt(portField.getText());
                } catch (NumberFormatException e) {
                    Window.alert("That port is not a number.");
                    return;
                }
                box.hide();
                BZNetwork.authLink.updateIrcBot(bot.getBotid(), nickField.getText(), serverField.getText(), portNumber, passwordField.getText(), channelField.getText(), new BoxCallback<Void>() {

                    @Override
                    public void run(Void result) {
                        select();
                    }
                });
            }
        });
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                box.hide();
            }
        });
    }
