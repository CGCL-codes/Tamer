        @Override
        public Iterator<Channel> getChannels() {
            ArrayList<Channel> ret = new ArrayList<Channel>(channels.values());
            return ret.iterator();
        }
