    @SuppressWarnings("deprecation")
    protected void select1(IrcBot[] result) {
        widget.clear();
        widget.add(new Header2("IRC Bots"));
        FlexTable table = new FlexTable();
        FlexCellFormatter format = table.getFlexCellFormatter();
        int row = 0;
        for (final IrcBot bot : result) {
            Label label = new Label(bot.getNick() + "@" + bot.getServer() + ":" + bot.getPort() + "/" + bot.getChannel());
            label.setTitle("Id: " + bot.getBotid());
            table.setWidget(row, 0, label);
            Anchor editLink = new Anchor("edit");
            editLink.setTitle("Opens up a box that you can use to edit this IRC bot.");
            table.setWidget(row, 1, editLink);
            editLink.addClickListener(new ClickListener() {

                @Override
                public void onClick(Widget sender) {
                    showUpdateBotDialog(bot);
                }
            });
            Anchor deleteLink = new Anchor("delete");
            deleteLink.setTitle("Deletes this IRC bot. Any triggers that use this bot " + "as the recipient will be deleted, and the bot will be " + "disconnected from the IRC server that it is currently " + "connected to.");
            table.setWidget(row, 2, deleteLink);
            deleteLink.addClickListener(new ClickListener() {

                @Override
                public void onClick(Widget sender) {
                    deleteIrcBot(bot);
                }
            });
            row += 1;
        }
        widget.add(table);
        Button addButton = new Button("Add");
        widget.add(addButton);
        widget.add(new Spacer("8px", "3px"));
        addButton.setTitle("Opens a dialog where you can add a new IRC bot.");
        addButton.addClickListener(new ClickListener() {

            @Override
            public void onClick(Widget sender) {
                IrcBot newBot = new IrcBot();
                newBot.setBotid(-1);
                newBot.setChannel("");
                newBot.setNick("");
                newBot.setPassword("");
                newBot.setPort(0);
                newBot.setServer("");
                showUpdateBotDialog(newBot);
            }
        });
        Button resyncButton = new Button("<b>Re-sync IRC connections</b>");
        resyncButton.setTitle("Disconnects and then re-connects all IRC bots. This can be " + "useful when the connected IRC bots have somehow gotten out " + "of sync with the actual list of IRC bots.");
        widget.add(resyncButton);
        resyncButton.addClickListener(new ClickListener() {

            @Override
            public void onClick(Widget sender) {
                resyncIrc();
            }
        });
    }
