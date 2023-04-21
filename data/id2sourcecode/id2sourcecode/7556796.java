        public void onQuit(String user, String nick, String txt) {
            for (Enumeration e = getChannels().elements(); e.hasMoreElements(); ) {
                Channel chan = (Channel) e.nextElement();
                chan.getChannelMux().onQuit(user, nick, txt);
            }
        }
