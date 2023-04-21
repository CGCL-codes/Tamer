        public void onOp(String oper, String chan, String oped) {
            getChannel(chan, true).getChannelMux().onOp(oper, chan, oped);
        }
