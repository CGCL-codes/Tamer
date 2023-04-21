        private List<Trigger> getTriggers(String server, String channel) {
            List<Trigger> res = new LinkedList<Trigger>();
            Set<Trigger> triggers = serversettings.get(server).triggers;
            synchronized (triggers) {
                for (Trigger t : triggers) if (t.getChannel().equals(channel)) res.add(t);
            }
            return res;
        }
