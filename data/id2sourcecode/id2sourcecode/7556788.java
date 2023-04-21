        public void onNick(String user, String oldnick, String newnick) {
            fireStatusEvent(oldnick + " now known as " + newnick);
            for (Enumeration e = getChannels().elements(); e.hasMoreElements(); ) {
                Channel chan = (Channel) e.nextElement();
                chan.getChannelMux().onNick(user, oldnick, newnick);
            }
        }
