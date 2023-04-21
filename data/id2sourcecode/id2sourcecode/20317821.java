    void handleChatControl(TChannelMessage msg) {
        TChatControl ctrlmsg = new TChatControl(msg.Data);
        TChatUserInfo u = People.findById(msg.SenderId);
        switch(msg.UserData) {
            case CHATCTRL_ENTER:
                if (msg.SenderId != Our.Id) {
                    if (ctrlmsg.ChannelId == CurrentChannelId) {
                        wrapit(ctrlmsg.Text + "\n");
                    }
                    addPeople(msg.SenderId, ctrlmsg.Name, ctrlmsg.ChannelId, ctrlmsg.Name + "'s channel");
                }
                sendControlMessage(CHATCTRL_ENTERACK, Our.Topic);
                break;
            case CHATCTRL_ENTERACK:
                addPeople(msg.SenderId, ctrlmsg.Name, ctrlmsg.ChannelId, ctrlmsg.Text);
                break;
            case CHATCTRL_LEAVE:
                deletePeople(u);
                if (ctrlmsg.ChannelId == CurrentChannelId) {
                    wrapit(ctrlmsg.Text + "\n");
                }
                break;
            case CHATCTRL_SWITCH:
                TChatControlSwitch switchmsg = new TChatControlSwitch(msg.Data);
                if (u != null) {
                    if (u.Channel == CurrentChannelId && msg.SenderId != Our.Id) {
                        wrapit(switchmsg.Text + "\n");
                    }
                    u.Channel = switchmsg.NewId;
                } else {
                    addPeople(msg.SenderId, switchmsg.Name, switchmsg.NewId, switchmsg.Name + "'s channel");
                }
                if (msg.SenderId != Our.Id) {
                    if (switchmsg.NewId == CurrentChannelId) {
                        wrapit(switchmsg.NewText + "\n");
                    } else {
                        if (u != null) {
                            peoplewindow.removeItem(u);
                        }
                    }
                }
                peoplewindow.clear();
                synchronized (People) {
                    int i = 0;
                    while (true) {
                        TChatUserInfo tu = People.findByIndex(i);
                        if (tu == null) {
                            break;
                        }
                        if (tu.Channel == CurrentChannelId) {
                            peoplewindow.addItem(tu.Name, tu);
                        }
                        i++;
                    }
                }
                break;
            case CHATCTRL_TOPIC:
                if (u != null) {
                    u.Topic = ctrlmsg.Text;
                    if (u.Channel == CurrentChannelId) {
                        if (msg.SenderId != Our.Id) {
                            wrapit(ctrlmsg.Name + " has just changed this channel topic to " + ctrlmsg.Text + "\n");
                        }
                        titlewindow.setText(getChannelName(CurrentChannelId));
                    }
                }
                break;
            case 0xFFFF:
                if (u != null) {
                    if (u.Channel == CurrentChannelId) {
                        wrapit(u.Name + " has just vanished without a trace!\n");
                    }
                    deletePeople(u);
                }
                break;
        }
    }
