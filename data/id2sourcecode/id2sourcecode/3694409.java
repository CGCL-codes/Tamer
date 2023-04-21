        private Set<String> getChannels(String server) {
            Set<String> res = new HashSet<String>();
            Set<Trigger> triggers = serversettings.get(server).triggers;
            synchronized (triggers) {
                for (Trigger t : triggers) res.add(t.getChannel());
            }
            return res;
        }
