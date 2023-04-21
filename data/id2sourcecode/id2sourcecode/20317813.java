    void doJoin(String args) {
        int newchannel;
        String msg;
        if (args == null) {
            if (CurrentChannelId != Our.Id) {
                newchannel = Our.Id;
                msg = "You just switched to your personal channel.";
            } else {
                newchannel = 0;
                msg = "You just switched to the main channel.";
            }
        } else if (args.equalsIgnoreCase("main")) {
            newchannel = 0;
            msg = "You just switched to the main channel.";
        } else {
            synchronized (People) {
                int n = People.findIndexByName(args, CurrentChannelId, true);
                if (n == -1) {
                    n = People.findIndexByTopic(args);
                }
                if (n == -1) {
                    int chid = PredefinedChannels.findIdByName(args);
                    if (chid == -1) {
                        wrapit("There is no user by the name of \"" + args + "\" in chat at this time, and no channel was found.\n");
                        return;
                    } else if (chid == -2) {
                        wrapit("The name \"" + args + "\" matches more than one chat channel.\n");
                        return;
                    }
                    newchannel = chid;
                    PredefinedChannel c = PredefinedChannels.findById(chid);
                    msg = "You just switched to the " + c.Name + " channel.";
                } else if (n == -2) {
                    wrapit("The name \"" + args + "\" matches more than one chat user.\n");
                    return;
                } else {
                    TChatUserInfo u = People.findByIndex(n);
                    newchannel = u.Id;
                    msg = "You just switched to " + u.Name + "'s private channel.";
                }
            }
        }
        if (newchannel == CurrentChannelId) {
            wrapit("You are already in that channel.\n");
            return;
        }
        CurrentChannel.close();
        CurrentChannel = openChannel("Chat." + newchannel);
        int oldchannel = CurrentChannelId;
        CurrentChannelId = newchannel;
        sendControlMessageSwitch(oldchannel, Our.Name + " has switched to another channel.", newchannel, Our.Name + " has switched to this channel.");
        chatwindow.setText("");
        wrapit(msg + "\n");
        titlewindow.setText(getChannelName(CurrentChannelId));
    }
