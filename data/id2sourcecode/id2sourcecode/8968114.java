    public void onEvent(Event event) {
        MobileBean mobileBean = (MobileBean) event.getAttribute("mobile-bean");
        if (mobileBean == null) {
            return;
        }
        String action = (String) event.getAttribute("action");
        if (!action.equalsIgnoreCase("create")) {
            return;
        }
        SyncContext context = (SyncContext) ExecutionContext.getInstance().getSyncContext();
        Session session = context.getSession();
        DeviceController deviceController = DeviceController.getInstance();
        String deviceId = session.getDeviceId();
        String channel = session.getChannel();
        String operation = ServerSyncEngine.OPERATION_ADD;
        String oid = Tools.getOid(mobileBean);
        String app = session.getApp();
        log.debug("*************************************");
        log.debug("Bean Added: " + oid);
        log.debug("DeviceId : " + deviceId);
        log.debug("Channel: " + channel);
        log.debug("Operation: " + operation);
        log.debug("App: " + app);
        Device device = deviceController.read(deviceId);
        if (device == null) {
            return;
        }
        Identity registeredUser = device.getIdentity();
        log.debug("User: " + registeredUser.getPrincipal());
        Set<Device> allDevices = deviceController.readByIdentity(registeredUser.getPrincipal());
        if (allDevices == null || allDevices.isEmpty()) {
            return;
        }
        Map<String, Notification> pushNotifications = new HashMap<String, Notification>();
        for (Device local : allDevices) {
            String myDeviceId = local.getIdentifier();
            log.debug("DeviceId: " + myDeviceId);
            if (myDeviceId.equals(deviceId)) {
                continue;
            }
            Set<String> apps = this.conflictEngine.findLiveApps(myDeviceId, channel);
            if (apps == null || apps.isEmpty()) {
                continue;
            }
            for (String subscribedApp : apps) {
                ChangeLogEntry changelogEntry = new ChangeLogEntry();
                changelogEntry.setTarget(myDeviceId);
                changelogEntry.setNodeId(channel);
                changelogEntry.setApp(subscribedApp);
                changelogEntry.setOperation(operation);
                changelogEntry.setRecordId(oid);
                boolean exists = this.syncEngine.changeLogEntryExists(changelogEntry);
                if (exists) {
                    continue;
                }
                List entries = new ArrayList();
                entries.add(changelogEntry);
                this.syncEngine.addChangeLogEntries(myDeviceId, subscribedApp, entries);
                Notification notification = Notification.createSilentSyncNotification(myDeviceId, channel);
                pushNotifications.put(myDeviceId, notification);
            }
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
