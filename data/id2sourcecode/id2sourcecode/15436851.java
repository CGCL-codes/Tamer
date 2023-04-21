    protected void buildOneDExpChannels(Pool xmlPool, DevicePool pool) {
        AttributeFilter filter = new AttributeFilter(currentPreferences.getChannelAttributeSaveLevel());
        for (OneDExpChannel onedExpChannel : pool.getOneDExpChannels()) {
            es.cells.sardana.client.framework.config.ExpChannel xmlOneDExpChannel = xmlPool.addNewOneDExpChannel();
            buildPollConfig(xmlOneDExpChannel, onedExpChannel);
            buildLogging(xmlOneDExpChannel, onedExpChannel);
            xmlOneDExpChannel.setAlias(onedExpChannel.getName());
            ControllerRef xmlCtrlRef = xmlOneDExpChannel.addNewControllerRef();
            xmlCtrlRef.setName(onedExpChannel.getController().getName());
            xmlCtrlRef.setIndex(onedExpChannel.getIdInController());
            onedExpChannel.getEventAttributes().refresh();
            onedExpChannel.getPolledAttributes().refresh();
            onedExpChannel.getNonPolledAttributes().refresh();
            for (AttributeInfoEx attr : onedExpChannel.getAttributeInfo()) {
                if (!filter.isValid(attr)) continue;
                buildAttribute(xmlOneDExpChannel, onedExpChannel, attr);
            }
        }
    }
