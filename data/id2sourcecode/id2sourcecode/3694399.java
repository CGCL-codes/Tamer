        protected void onConnect() {
            settings.triggers = fservTriggers;
            settings.xdccpacks = xdccpacks;
            String nspw = cfg.getNickServPassword();
            if (nspw != null && nspw.length() != 0) {
                if (!getNick().equals(cfg.getNickname())) {
                }
                if (getNick().equals(cfg.getNickname())) {
                    sendMessage("nickserv", "IDENTIFY " + nspw);
                }
            }
            for (ChannelConfig chan : cfg.getChannels()) {
                channelMap.put(chan.getName(), chan);
                channelStamps.put(chan, new Long(0));
                String pass = chan.getKey();
                if (pass != null && pass.length() != 0) joinChannel(chan.getName(), pass); else joinChannel(chan.getName());
            }
        }
