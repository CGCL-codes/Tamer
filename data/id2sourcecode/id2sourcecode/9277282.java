    public void channelUpdated(ChannelEvent event) {
        List<ChannelBeanMetaData> updateInfo = (List<ChannelBeanMetaData>) event.getAttribute(ChannelEvent.metadata);
        for (ChannelBeanMetaData cour : updateInfo) {
            log.info("ChannelEvent---------------------------------------------");
            log.info("Channel: " + cour.getChannel());
            log.info("BeanId: " + cour.getBeanId());
            log.info("UpdateType: " + cour.getUpdateType());
            log.info("---------------------------------------------------------");
        }
    }
