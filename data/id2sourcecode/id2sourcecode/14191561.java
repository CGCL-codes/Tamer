        public void dataChanged(Object source) {
            int row = ircChannelTable.getSelectedRow();
            if (row < 0 || row >= ircChannelTable.getRowCount()) return;
            row = ircChannelSorter.indexes[row];
            Vector list = ircMgr.getChannels();
            if (row >= list.size()) {
                return;
            }
            IrcChannel channel = (IrcChannel) list.elementAt(row);
            if (channel != (IrcChannel) source) {
                return;
            }
            String logText = channel.getLastMsgText();
            if (logText.startsWith(ServiceManager.getCfg().mIrcTopicString)) {
                channel.setTopic(logText.substring(ServiceManager.getCfg().mIrcTopicString.length()).trim());
                ircChannelStatus.setText(channel.getName() + ": " + channel.getTopic());
            }
            if (!logText.startsWith(ServiceManager.getCfg().mIrcFilterString)) {
                try {
                    ircChatLogDoc.insertString(ircChatLogDoc.getLength(), logText, ircChatLogStyle);
                } catch (BadLocationException ex) {
                    ;
                }
                if (ircAutoScrollChatText.isSelected() && logText.length() > 0) ircChatLogText.setCaretPosition(ircChatLogDoc.getLength());
            }
        }
