    public void onEvent(Event event) {
        MobileBean mobileBean = (MobileBean) event.getAttribute("mobile-bean");
        if (mobileBean == null) {
            return;
        }
        String action = (String) event.getAttribute("action");
        if (!action.equalsIgnoreCase("update")) {
            return;
        }
        SyncContext context = (SyncContext) ExecutionContext.getInstance().getSyncContext();
        Session session = context.getSession();
        String deviceId = session.getDeviceId();
        String channel = session.getChannel();
        String operation = ServerSyncEngine.OPERATION_UPDATE;
        String oid = Tools.getOid(mobileBean);
        String app = session.getApp();
        log.debug("*************************************");
        log.debug("Bean Updated: " + oid);
        log.debug("DeviceId : " + deviceId);
        log.debug("Channel: " + channel);
        log.debug("Operation: " + operation);
        log.debug("App: " + app);
        List<ConflictEntry> liveEntries = this.conflictEngine.findLiveEntries(channel, oid);
        if (liveEntries == null || liveEntries.isEmpty()) {
            return;
        }
        Map<String, Notification> pushNotifications = new HashMap<String, Notification>();
        for (ConflictEntry entry : liveEntries) {
            if (entry.getDeviceId().equals(deviceId) && entry.getApp().equals(app) && entry.getChannel().equals(channel) && entry.getOid().equals(oid)) {
                continue;
            }
            ChangeLogEntry changelogEntry = new ChangeLogEntry();
            changelogEntry.setTarget(entry.getDeviceId());
            changelogEntry.setNodeId(entry.getChannel());
            changelogEntry.setApp(entry.getApp());
            changelogEntry.setOperation(operation);
            changelogEntry.setRecordId(entry.getOid());
            boolean exists = this.syncEngine.changeLogEntryExists(changelogEntry);
            if (exists) {
                continue;
            }
            List entries = new ArrayList();
            entries.add(changelogEntry);
            this.syncEngine.addChangeLogEntries(entry.getDeviceId(), entry.getApp(), entries);
            Notification notification = Notification.createSilentSyncNotification(entry.getDeviceId(), channel);
            pushNotifications.put(entry.getDeviceId(), notification);
        }
        if (pushNotifications.isEmpty()) {
            return;
        }
        Set<String> deviceIds = pushNotifications.keySet();
        for (String id : deviceIds) {
            Notification notification = pushNotifications.get(id);
            log.debug("Notification----------------------------------------------");
            log.debug("Device: " + notification.getMetaDataAsString("device") + ", Channel: " + notification.getMetaDataAsString("service"));
            log.debug("----------------------------------------------");
            this.notifier.process(notification);
        }
        log.debug("*************************************");
    }
