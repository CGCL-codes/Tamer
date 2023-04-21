    public Map<String, Object> dumpStats() {
        Map<String, Object> retval = mux.getChannel().getProtocolStack().dumpStats();
        if (retval != null) {
            Map<String, Long> tmp = dumpChannelStats();
            if (tmp != null) retval.put("channel", tmp);
        }
        return retval;
    }
