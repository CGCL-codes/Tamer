    public void serverPush(NetworkEvent event) {
        List<ChannelBeanMetaData> metadata = (List<ChannelBeanMetaData>) event.getAttribute(event.metadata);
        if (metadata != null) {
            List<Notification> notifications = new ArrayList<Notification>();
            Set<String> cour = new HashSet<String>();
            for (ChannelBeanMetaData info : metadata) {
                String device = info.getDeviceId();
                String channel = info.getChannel();
                cour.add(device + "=" + channel);
            }
            for (String notificationData : cour) {
                int index = notificationData.indexOf('=');
                String device = notificationData.substring(0, index);
                String channel = notificationData.substring(index + 1);
                Notification notification = Notification.createSyncNotification(device, channel);
                notifications.add(notification);
            }
            if (!notifications.isEmpty()) {
                for (Notification notification : notifications) {
                    log.debug("Notification----------------------------------------------");
                    log.debug("Device: " + notification.getMetaDataAsString("device") + ", Channel: " + notification.getMetaDataAsString("service"));
                    log.debug("----------------------------------------------");
                    this.notifier.process(notification);
                }
            }
        }
    }
