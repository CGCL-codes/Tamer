    protected void addChannelMenuItems(Menu menu, String word) {
        if (connector.isLikelyChannel(word)) {
            if (menu.getItemCount() > 0) {
                new MenuItem(menu, SWT.SEPARATOR);
            }
            final String channel = connector.parseChannel(word);
            MenuItem item = null;
            item = new MenuItem(menu, SWT.PUSH);
            item.setText(local.getString("chatConsCont14") + channel);
            item.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event e) {
                    if (!Raptor.getInstance().getWindow().containsChannelItem(connector, channel)) {
                        ChatConsoleWindowItem windowItem = new ChatConsoleWindowItem(new ChannelController(connector, channel));
                        Raptor.getInstance().getWindow().addRaptorWindowItem(windowItem, false);
                        ChatUtils.appendPreviousChatsToController(windowItem.console);
                    }
                }
            });
            final String[][] connectorChannelItems = connector.getChannelActions(channel);
            if (connectorChannelItems != null) {
                MenuItem channelItem = new MenuItem(menu, SWT.CASCADE);
                channelItem.setText(connector.getShortName() + local.getString("chatConsCont15") + channel);
                Menu channelItemMenu = new Menu(menu);
                channelItem.setMenu(channelItemMenu);
                for (int i = 0; i < connectorChannelItems.length; i++) {
                    item = new MenuItem(channelItemMenu, SWT.PUSH);
                    item.setText(connectorChannelItems[i][0]);
                    final int index = i;
                    item.addListener(SWT.Selection, new Listener() {

                        public void handleEvent(Event e) {
                            connector.sendMessage(connectorChannelItems[index][1]);
                        }
                    });
                }
            }
        }
    }
