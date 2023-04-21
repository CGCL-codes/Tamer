    public static List<ChannelPoolMXBean> getChannelPoolMXBeans() {
        List<ChannelPoolMXBean> result = new LinkedList<ChannelPoolMXBean>();
        Object[] providers = new Object[] { SelectorProvider.provider(), AsynchronousChannelProvider.provider() };
        for (Object provider : providers) {
            if (provider instanceof ManagedChannelFactory) {
                result.addAll(((ManagedChannelFactory) provider).getChannelPoolMXBeans());
            }
        }
        return result;
    }
