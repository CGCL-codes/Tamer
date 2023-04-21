        public void onBan(String banned, String chan, String banner) {
            getChannel(chan, true).getChannelMux().onBan(banned, chan, banner);
        }
