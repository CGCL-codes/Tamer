    public void channelUpdated(ChannelEvent event) {
        List<ChannelBeanMetaData> updateInfo = (List<ChannelBeanMetaData>) event.getAttribute(ChannelEvent.metadata);
        if (updateInfo != null) {
            Map<String, List> changeLogMap = new HashMap<String, List>();
            for (ChannelBeanMetaData cour : updateInfo) {
                String deviceId = cour.getDeviceId();
                String beanId = cour.getBeanId();
                String channel = cour.getChannel();
                ChannelUpdateType updateType = cour.getUpdateType();
                List changelog = this.findChangeLog(changeLogMap, deviceId);
                ChangeLogEntry entry = new ChangeLogEntry();
                entry.setNodeId(channel);
                entry.setTarget(deviceId);
                entry.setRecordId(beanId);
                if (updateType == ChannelUpdateType.ADD) {
                    entry.setOperation(ServerSyncEngine.OPERATION_ADD);
                } else if (updateType == ChannelUpdateType.REPLACE) {
                    entry.setOperation(ServerSyncEngine.OPERATION_UPDATE);
                } else if (updateType == ChannelUpdateType.DELETE) {
                    entry.setOperation(ServerSyncEngine.OPERATION_DELETE);
                }
                changelog.add(entry);
            }
            Set<String> updatedDevices = changeLogMap.keySet();
            for (String updatedDevice : updatedDevices) {
                List changelogEntries = changeLogMap.get(updatedDevice);
                this.updateChangeLog(updatedDevice, changelogEntries);
            }
        }
    }
