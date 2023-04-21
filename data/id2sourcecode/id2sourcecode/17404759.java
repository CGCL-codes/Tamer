    private void onPart(String host, String channel) {
        Channel chanRecord = getChannel(channel, false);
        if (chanRecord == null) {
            libairc.debug("Part", "Unknown channel: " + channel);
            return;
        }
        if (libairc.getNickFromHost(host).equalsIgnoreCase(nick)) {
            chanRecord.cleanUsers();
            synchronized (channels) {
                channels.remove(channel);
            }
        } else {
            libairc.debug("Part", "Nick: " + libairc.getNickFromHost(host) + ", Channel: " + channel);
            chanRecord.remUser(libairc.getNickFromHost(host));
            Object[] chans;
            String nick = libairc.getNickFromHost(host);
            boolean contained = false;
            synchronized (channels) {
                chans = channels.keySet().toArray();
                for (int index = 0; index < chans.length && !contained; index++) {
                    if (((Channel) channels.get(chans[index])).containsUser(nick)) {
                        contained = true;
                    }
                }
                if (!contained) {
                    libairc.debug("Part", "User '" + nick + "' not found in any channels...deleting");
                    synchronized (users) {
                        users.remove(nick);
                    }
                }
            }
        }
    }
