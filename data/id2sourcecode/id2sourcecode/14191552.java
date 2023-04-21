        public void valueChanged(ListSelectionEvent e) {
            if (ircChannelChanged) return;
            int row = ircChannelTable.getSelectedRow();
            if (row < 0 || row >= ircChannelTable.getRowCount()) return;
            row = ircChannelSorter.indexes[row];
            Vector list = ircMgr.getChannels();
            if (row >= list.size()) {
                return;
            }
            try {
                ((IrcChannel) list.elementAt(ircCurrentChannel)).setIsCurrent(false);
            } catch (Exception e2) {
            }
            IrcChannel channel = (IrcChannel) list.elementAt(row);
            channel.setIsCurrent(true);
            ircCurrentChannel = row;
            String logText = channel.getChatLogText(false);
            try {
                ircChatLogText.setDocument(new DefaultStyledDocument());
                ircChatLogDoc = (StyledDocument) ircChatLogText.getDocument();
                ircChatLogDoc.insertString(0, logText, ircChatLogStyle);
            } catch (BadLocationException ex) {
                ;
            }
            if (ircAutoScrollChatText.isSelected() && logText.length() > 0) {
                ircChatLogText.setCaretPosition(ircChatLogDoc.getLength());
            }
            ircChannelStatus.setText(channel.getName() + ": " + channel.getTopic());
            ((IrcUserListModel) ircUsersModel).fireChanged(channel);
            mainFrame.refreshAllActions();
        }
