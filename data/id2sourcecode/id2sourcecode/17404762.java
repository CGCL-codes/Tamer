    private void onChanMsg(String host, String channel, String message) {
        User user = getUser(libairc.getNickFromHost(host));
        Channel chanRec = getChannel(channel, false);
        if (chanRec == null) {
            return;
        }
        if (message.startsWith(libairc.CTCP + "ACTION ") && message.endsWith(libairc.CTCP)) {
            events.add(new ChannelMessageInvoker(this, user, chanRec, message.substring(8, message.length() - 1), true, lsnChanMsgs));
        } else {
            events.add(new ChannelMessageInvoker(this, user, chanRec, message, false, lsnChanMsgs));
        }
    }
