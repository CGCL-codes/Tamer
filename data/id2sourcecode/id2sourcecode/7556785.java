        public void onKick(String kicked, String chan, String kicker, String txt) {
            getChannel(chan, true).getChannelMux().onKick(kicked, chan, kicker, txt);
        }
