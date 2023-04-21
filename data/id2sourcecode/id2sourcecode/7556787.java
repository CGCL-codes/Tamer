        public void onPrivateMessage(String orgnick, String chan, String txt) {
            getChannel(chan, true).getChannelMux().onPrivateMessage(orgnick, chan, txt);
        }
