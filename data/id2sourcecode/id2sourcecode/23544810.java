    protected void buildCTExpChannels(Pool xmlPool, DevicePool pool) {
        AttributeFilter filter = new AttributeFilter(currentPreferences.getChannelAttributeSaveLevel());
        for (CounterTimer ctExpChannel : pool.getCounterTimers()) {
            ExpChannel xmlCTExpChannel = xmlPool.addNewCTExpChannel();
            xmlCTExpChannel.setAlias(ctExpChannel.getName());
            buildPollConfig(xmlCTExpChannel, ctExpChannel);
            buildLogging(xmlCTExpChannel, ctExpChannel);
            ControllerRef xmlCtrlRef = xmlCTExpChannel.addNewControllerRef();
            xmlCtrlRef.setName(ctExpChannel.getController().getName());
            xmlCtrlRef.setIndex(ctExpChannel.getIdInController());
            ctExpChannel.getEventAttributes().refresh();
            ctExpChannel.getPolledAttributes().refresh();
            ctExpChannel.getNonPolledAttributes().refresh();
            for (AttributeInfoEx attr : ctExpChannel.getAttributeInfo()) {
                if (!filter.isValid(attr)) continue;
                buildAttribute(xmlCTExpChannel, ctExpChannel, attr);
            }
        }
    }
