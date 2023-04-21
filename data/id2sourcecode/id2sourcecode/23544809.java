    protected void buildZeroDExpChannels(Pool xmlPool, DevicePool pool) {
        AttributeFilter filter = new AttributeFilter(currentPreferences.getChannelAttributeSaveLevel());
        for (ZeroDExpChannel zerodExpChannel : pool.getZeroDExpChannels()) {
            es.cells.sardana.client.framework.config.ExpChannel xmlZeroDExpChannel = xmlPool.addNewZeroDExpChannel();
            buildPollConfig(xmlZeroDExpChannel, zerodExpChannel);
            buildLogging(xmlZeroDExpChannel, zerodExpChannel);
            xmlZeroDExpChannel.setAlias(zerodExpChannel.getName());
            ControllerRef xmlCtrlRef = xmlZeroDExpChannel.addNewControllerRef();
            xmlCtrlRef.setName(zerodExpChannel.getController().getName());
            xmlCtrlRef.setIndex(zerodExpChannel.getIdInController());
            zerodExpChannel.getEventAttributes().refresh();
            zerodExpChannel.getPolledAttributes().refresh();
            zerodExpChannel.getNonPolledAttributes().refresh();
            for (AttributeInfoEx attr : zerodExpChannel.getAttributeInfo()) {
                if (!filter.isValid(attr)) continue;
                buildAttribute(xmlZeroDExpChannel, zerodExpChannel, attr);
            }
        }
    }
