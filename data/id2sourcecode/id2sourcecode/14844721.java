                public void run() {
                    Room[] channels = client.getClientState().getChannels();
                    if (event instanceof ConnectedEvent) {
                        ConnectedEvent connected = (ConnectedEvent) event;
                        channelList.setListData(channels);
                        String text = connected.getText();
                        chatArea.append(text + "\n");
                        channelList.setSelectedValue(channels[0], true);
                    } else if (event instanceof UpdateChatEvent) {
                        UpdateChatEvent update = (UpdateChatEvent) event;
                        String text = update.getText();
                        chatArea.append(text + "\n");
                    } else if (event instanceof NameReplyEvent) {
                        NameReplyEvent nameRply = (NameReplyEvent) event;
                        membersList.setListData(nameRply.getChannelMembers());
                        channelList.setListData(channels);
                        String channelName = nameRply.getChannel();
                        Room channel = client.getClientState().getChannel(channelName);
                        channelList.setSelectedValue(channel, true);
                    }
                }
