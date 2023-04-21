    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        for (; ; ) {
            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException e) {
                Logging.warn("EXPIRY", "Thread interrupted.");
                break;
            }
            Logging.warn("EXPIRY", "Checking for expired channels...");
            ArrayList<SrvChannel_channel> deletelist = new ArrayList<SrvChannel_channel>();
            for (SrvChannel_channel chan : ((SrvChannel) Configuration.getSvc().get(Configuration.chanservice)).getChannels().values()) {
                if (chan.getAllMeta().containsKey("_ts_last")) {
                    if (Long.parseLong(util.getTS()) - Long.parseLong(chan.getMeta("_ts_last")) > (60 * 60 * 24 * 7 * 5)) {
                        deletelist.add(chan);
                    }
                } else {
                    chan.setMeta("_ts_last", util.getTS());
                }
            }
            for (SrvChannel_channel chan : deletelist) {
                ((SrvChannel) Configuration.getSvc().get(Configuration.chanservice)).getChannels().remove(chan.channel);
                Generic.srvPart(Configuration.getSvc().get(Configuration.chanservice), chan.channel, "Channel Expired.");
            }
            Logging.info("EXPIRY", deletelist.size() + " of " + (((SrvChannel) Configuration.getSvc().get(Configuration.chanservice)).getChannels().size()) + " channels expired due to inactivity...");
        }
        util.getThreads().remove(Thread.currentThread());
    }
