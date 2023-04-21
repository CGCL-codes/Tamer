        public void preShowChange() {
            for (Channel channel : getShow().getChannels()) {
                channel.removeNameListener(channelNameListener);
            }
            for (int i = 0; i < getShow().getNumberOfSubmasters(); i++) {
                Submaster submaster = getShow().getSubmasters().get(i);
                submaster.removeNameListener(submasterNameListener);
            }
        }
