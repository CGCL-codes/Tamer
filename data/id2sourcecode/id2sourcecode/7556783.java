        public void onJoin(String user, String nick, String chan, boolean create) {
            getChannel(chan, true).getChannelMux().onJoin(user, nick, chan, create);
        }
