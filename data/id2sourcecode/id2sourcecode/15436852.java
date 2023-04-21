    protected void buildTwoDExpChannels(Pool xmlPool, DevicePool pool) {
        AttributeFilter filter = new AttributeFilter(currentPreferences.getChannelAttributeSaveLevel());
        for (TwoDExpChannel twodExpChannel : pool.getTwoDExpChannels()) {
            es.cells.sardana.client.framework.config.ExpChannel xmlTwoDExpChannel = xmlPool.addNewTwoDExpChannel();
            buildPollConfig(xmlTwoDExpChannel, twodExpChannel);
            buildLogging(xmlTwoDExpChannel, twodExpChannel);
            xmlTwoDExpChannel.setAlias(twodExpChannel.getName());
            ControllerRef xmlCtrlRef = xmlTwoDExpChannel.addNewControllerRef();
            xmlCtrlRef.setName(twodExpChannel.getController().getName());
            xmlCtrlRef.setIndex(twodExpChannel.getIdInController());
            twodExpChannel.getEventAttributes().refresh();
            twodExpChannel.getPolledAttributes().refresh();
            twodExpChannel.getNonPolledAttributes().refresh();
            for (AttributeInfoEx attr : twodExpChannel.getAttributeInfo()) {
                if (!filter.isValid(attr)) continue;
                buildAttribute(xmlTwoDExpChannel, twodExpChannel, attr);
            }
        }
    }
