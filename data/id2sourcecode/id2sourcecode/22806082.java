    public UIListener generateUIListener() {
        return new UIListener() {

            public void sendCommand(IRCMessage m) {
                myInterpreter.sendData(m);
            }

            public boolean isValidCommand(IRCMessage m) {
                if (m == null) return false;
                return true;
            }

            public IRCMessage constructMessage(String commandline) {
                return doConstructMessage(commandline);
            }

            public void connect() {
                doConnect();
            }

            public void disconnect() {
                doDisconnect();
            }

            public void setParameter(String key, String val) {
                addParam(key, val);
            }

            public String getParameter(String key) {
                return getParamValue(key);
            }

            public void saveParams() {
                saveData();
            }

            public void login() {
                if (connected) doLogin();
            }

            public OJIRCChannel getChannel(String ch) {
                return getTheChannel(ch);
            }

            public String[] getChannels() {
                return getChannelsList();
            }

            public String[] getChannelMembers(String chname) {
                return getChannelMembersNicknames(chname);
            }

            public int getChannelCount() {
                return getChannelsCount();
            }

            public boolean getConnected() {
                return connected;
            }
        };
    }
