    public static void unregisterChannel(JChannel c, MBeanServer server, String clusterName) throws Exception {
        ProtocolStack stack = c.getProtocolStack();
        Vector<Protocol> protocols = stack.getProtocols();
        for (Protocol p : protocols) {
            if (p.getClass().isAnnotationPresent(MBean.class)) {
                try {
                    unregister(p, server, getProtocolRegistrationName(clusterName, "jgroups", p));
                } catch (MBeanRegistrationException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("MBean unregistration failed " + e);
                    }
                }
            }
        }
        unregister(c, server, getChannelRegistrationName(clusterName));
    }
