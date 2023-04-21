            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == 13) {
                    String cmd = stInput.getText();
                    if (cmd.startsWith("/")) {
                        ScriptManager.ParseCmd(stInput.getText());
                        stInput.setText("");
                    } else {
                        MessageQueue.addQueue(MessageQueueEnum.MSG_CHANNEL_OUT, new ChannelMessageResponse(getChannel(), Connection.getUserInfo(), stInput.getText().replace("\r\n", "")));
                        getEmInput().Clear();
                    }
                }
            }
