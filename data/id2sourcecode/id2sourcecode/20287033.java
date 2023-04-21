    public static void registerChannel(JChannel channel, MBeanServer server, String domain, String cluster_name, boolean register_protocols) throws Exception {
        if (cluster_name == null) cluster_name = channel != null ? channel.getClusterName() : null;
        if (cluster_name == null) cluster_name = "null";
        if (register_protocols) {
            ProtocolStack stack = channel.getProtocolStack();
            Vector<Protocol> protocols = stack.getProtocols();
            for (Protocol p : protocols) {
                register(p, server, getProtocolRegistrationName(cluster_name, domain, p));
            }
        }
        register(channel, server, getChannelRegistrationName(channel, domain, cluster_name));
    }
