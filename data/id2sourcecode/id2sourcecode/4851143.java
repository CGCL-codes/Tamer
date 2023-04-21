        public void postShowChange() {
            for (Channel channel : getShow().getChannels()) {
                channel.addNameListener(channelNameListener);
            }
            for (int i = 0; i < getShow().getNumberOfSubmasters(); i++) {
                Submaster submaster = getShow().getSubmasters().get(i);
                submaster.addNameListener(submasterNameListener);
            }
            fireTableDataChanged();
        }
