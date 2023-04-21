    boolean start() {
        try {
            SystemInfo = WcProxy.getSystemInfo();
            setTitle("Chat - " + SystemInfo.BBSName);
            TGetCurrentUser_Response ur = WcProxy.getCurrentUser();
            Our = new TChatUserInfo();
            Our.Name = WordUtils.UpperLower(ur.User.Info.Name);
            TWhosOnline_Response wr = WcProxy.getNodeInfoByName(ur.User.Info.Name);
            Our.Id = wr.NodeInfo.ConnectionId;
            Our.Topic = Our.Name + "'s channel";
            SystemPageChannel = openChannel("System.Page");
            ChatControl = openChannel("Chat.Control");
            CurrentChannel = openChannel("Chat.0");
            CurrentChannelId = 0;
            sendControlMessage(CHATCTRL_ENTER, Our.Name + " has just entered the chat room.");
            PredefinedChannelsSocket = WcProxy.sendRequest(new TWildcatRequest(WildcatRequest.wrGetPredefinedChatChannels), this);
            titlewindow.setText(getChannelName(CurrentChannelId));
        } catch (IOException iox) {
            System.out.println("Startup error: " + iox.getMessage());
            return false;
        }
        return true;
    }
