        private void scan(Device device, Date timestamp, List<ChannelBeanMetaData> allUpdates) {
            try {
                LastScanTimestamp lastScanTimestamp = this.findScanTimestamp(device);
                Identity identity = device.getIdentity();
                String[] added = this.channelRegistration.getChannel().scanForNew(device, lastScanTimestamp.getTimestamp());
                String[] updated = this.channelRegistration.getChannel().scanForUpdates(device, lastScanTimestamp.getTimestamp());
                String[] deleted = this.channelRegistration.getChannel().scanForDeletions(device, lastScanTimestamp.getTimestamp());
                if (added != null) {
                    for (String beanId : added) {
                        ChannelBeanMetaData cour = new ChannelBeanMetaData();
                        cour.setChannel(this.channelRegistration.getUri());
                        cour.setBeanId(beanId);
                        cour.setDeviceId(device.getIdentifier());
                        cour.setUpdateType(ChannelUpdateType.ADD);
                        cour.setPrincipal(identity.getPrincipal());
                        allUpdates.add(cour);
                    }
                }
                if (updated != null) {
                    for (String beanId : updated) {
                        ChannelBeanMetaData cour = new ChannelBeanMetaData();
                        cour.setChannel(this.channelRegistration.getUri());
                        cour.setBeanId(beanId);
                        cour.setDeviceId(device.getIdentifier());
                        cour.setUpdateType(ChannelUpdateType.REPLACE);
                        cour.setPrincipal(identity.getPrincipal());
                        allUpdates.add(cour);
                    }
                }
                if (deleted != null) {
                    for (String beanId : deleted) {
                        ChannelBeanMetaData cour = new ChannelBeanMetaData();
                        cour.setChannel(this.channelRegistration.getUri());
                        cour.setBeanId(beanId);
                        cour.setDeviceId(device.getIdentifier());
                        cour.setUpdateType(ChannelUpdateType.DELETE);
                        cour.setPrincipal(identity.getPrincipal());
                        allUpdates.add(cour);
                    }
                }
                lastScanTimestamp.setTimestamp(timestamp);
                this.save(lastScanTimestamp);
            } catch (Exception e) {
                ErrorHandler.getInstance().handle(e);
                DateFormat dateFormat = DateFormat.getDateTimeInstance();
                Exception ex = new Exception("Device:" + device.getIdentifier() + ",Identity:" + device.getIdentity().getPrincipal() + "Channel: " + this.channelRegistration.getUri() + "Scan Time: " + dateFormat.format(new Date()));
                ErrorHandler.getInstance().handle(e);
            }
        }
