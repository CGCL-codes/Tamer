    private static IOListener copy(final IOListener l) {
        final boolean listenToAllChannels = l.listenToAllChannels();
        final Component targetComponent = l.getTargetComponent();
        final IOChannels iOChannels = l.getDataType();
        final K8055Channel k8055Channel = l.getChannel();
        IOListener ret = new IOListener() {

            @Override
            public boolean listenToAllChannels() {
                return listenToAllChannels;
            }

            @Override
            public Component getTargetComponent() {
                return targetComponent;
            }

            @Override
            public IOChannels getDataType() {
                return iOChannels;
            }

            @Override
            public K8055Channel getChannel() {
                return k8055Channel;
            }
        };
        return ret;
    }
