    public static void processRawText(NetworkClient from, ByteBuffer bb) {
        String msg = freelands.protocol.message.toserver.RawMessage.extractString(bb, 3, bb.getShort(1) - 1);
        char firstChar = msg.charAt(0);
        if (STARTCOMMAND == firstChar) {
            String values[] = msg.split(" ", 2);
            String command = values[0].substring(1);
            String args = values.length == 1 ? "" : values[1];
            try {
                InGameCommand igc = InGameCommand.valueOf(command);
                if (igc.isGeneralCommand) {
                    igc.process(from, args);
                } else {
                    from.addMessageToPlayer(bb);
                }
            } catch (IllegalArgumentException npe) {
                from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "Unknow command"));
            }
        } else if (STARTCHANNELCOMMAND == firstChar) {
            if (msg.length() == 1) {
                return;
            }
            char c2 = msg.charAt(1);
            int channel;
            if ((c2 == STARTCHANNELCOMMAND || c2 == FRENCHSTARTCHANNELCOMMAND) && msg.length() > 2) {
                String[] values = msg.split(" ", 2);
                if (values.length != 2) {
                    from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "Usage : !!number message"));
                    return;
                } else {
                    try {
                        channel = Integer.valueOf(values[0].substring(2));
                    } catch (Exception nfe) {
                        from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "Usage : !!number message"));
                        return;
                    }
                    if (!from.isInChannel(channel)) {
                        from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "You are not connected at this channel"));
                        return;
                    }
                    msg = values[1];
                }
            } else {
                channel = from.getActiveChatChannel();
                msg = msg.substring(1);
            }
            if (channel < 0) {
                from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "You're not connected at a channel"));
                return;
            }
            Channel.getChannel(channel).sendAtAll("[" + from.player.getContent().name + "]:" + msg);
        } else {
            from.addMessageToPlayer(bb);
        }
    }
