    private ObjectName getObjectName(Channel channel) throws JMException {
        ComponentModuleConfig smc = ComponentModuleConfig.getInstance();
        if (smc != null) {
            Hashtable ht = new Hashtable(2);
            ht.put("channel", channel.getChannelName());
            ht.put("module", smc.getName());
            ObjectName objName = ObjectName.getInstance("openfrwk.core", ht);
            return objName;
        }
        return ObjectName.getInstance("openfrwk.core", "channel", channel.getChannelName());
    }
