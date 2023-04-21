            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    autoscroll = true;
                    autoScroll(sv, svl);
                    String input = in.getText().toString();
                    in.setText("");
                    if (input.startsWith("/")) {
                        String[] temp = input.substring(1).split(" ");
                        String[] args = new String[temp.length - 1];
                        for (int i = 1; i < temp.length; i++) {
                            args[i - 1] = temp[i];
                        }
                        parseCommand(temp[0], args);
                        String out = "";
                        for (String part : args) {
                            out = out + part + ", ";
                        }
                    } else {
                        try {
                            String channel = getChannel();
                            if (channels.get(channel).isMute()) {
                                println(chatbot, "This channel is currently muted");
                            } else if (channels.get(channel).getID().length < 5) {
                                chatbot.sendPrivateChannelMessage(channel, input);
                            } else {
                                chatbot.sendChannelMessage(channel, input);
                            }
                        } catch (Exception e) {
                            exception(chatbot, e);
                        }
                    }
                    return true;
                }
                return false;
            }
