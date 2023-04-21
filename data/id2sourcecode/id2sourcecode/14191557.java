        public Object getValueAt(int row, int col) {
            Vector list = ircMgr.getChannels();
            if (row >= list.size()) {
                return "";
            }
            IrcChannel channel = (IrcChannel) list.elementAt(row);
            switch(col) {
                case 0:
                    {
                        return channel.getDescription();
                    }
                case 1:
                    {
                        return (channel.getJoined() ? "yes" : "no");
                    }
                case 2:
                    {
                        return (channel.getJoined() ? "" + channel.getUserCount() : "");
                    }
                case 3:
                    {
                        return (channel.getJoined() ? "" + channel.getMsgCountStr() : "");
                    }
            }
            return "";
        }
