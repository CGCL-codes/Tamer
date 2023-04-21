    public static List<Trigger> createTriggerForTables(String[] tables, Channel c) {
        List<Trigger> ret = new ArrayList<Trigger>();
        for (String t : tables) {
            Trigger obj = new Trigger();
            obj.setTriggerId(t);
            obj.setSourceTableName(t);
            obj.setChannelId(c.getChannelId());
            obj.setCreateTime(new Date());
            obj.setLastUpdateTime(new Date());
            ret.add(obj);
        }
        return ret;
    }
