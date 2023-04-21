        public void run() {
            open();
            log.debug(this);
            getPeersInfo();
            Context.getInstance().getChannelManager().accept(Channel.this);
        }
