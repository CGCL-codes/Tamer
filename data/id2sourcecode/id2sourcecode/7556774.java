        public void onAction(String user, String chan, String txt) {
            getChannel(chan, true).getChannelMux().onAction(user, chan, txt);
        }
