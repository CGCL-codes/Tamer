        public void performChannelEnd(final TVChannel currentChannel) {
            TVChannelsSet.Channel ch = new TVChannelsSet.Channel(currentChannel.getID(), currentChannel.getDisplayName());
            if (!info.channelsList.contains(ch.getChannelID())) {
                info.channelsList.add(ch);
            }
        }
