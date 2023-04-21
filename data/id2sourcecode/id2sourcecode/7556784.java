        public void onJoins(String users, String chan) {
            getChannel(chan, true).getChannelMux().onJoins(users, chan);
        }
