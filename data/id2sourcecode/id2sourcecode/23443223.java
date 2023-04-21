    private void checkConfiguration(Volantis volantisBean, MPSPluginConfigValue mps) {
        MpsPluginConfiguration config = (MpsPluginConfiguration) volantisBean.getApplicationPluginConfiguration("MPS");
        String ibu = config.getInternalBaseUrl();
        String mri = config.getMessageRecipientInfo();
        assertEquals("internal-base-url", mps.internalBaseUrl, ibu);
        assertEquals("message-recipient-info", mps.messageRecipientInfo, mri);
        Map channelTable = new HashMap();
        Iterator channelsIterator = config.getChannelsIterator();
        while (channelsIterator.hasNext()) {
            MpsChannelConfiguration channelConfig = (MpsChannelConfiguration) channelsIterator.next();
            channelTable.put(channelConfig.getName(), channelConfig);
        }
        Iterator channels = mps.channels.iterator();
        while (channels.hasNext()) {
            ConfigValueChannel channel = (ConfigValueChannel) channels.next();
            MpsChannelConfiguration channelConfig = (MpsChannelConfiguration) channelTable.get(channel.name);
            Map attrs = channelConfig.getArguments();
            assertNotNull("Channel Config for " + channel.name, channelConfig);
            assertEquals(channel.name, channelConfig.getName());
            assertEquals(channel.channelClass, channelConfig.getClassName());
            if (channel instanceof ConfigValueChannelSms) {
                ConfigValueChannelSms sms = (ConfigValueChannelSms) channel;
                assertEquals(sms.address, attrs.get(LogicaSMSChannelAdapter.ADDRESS));
                assertEquals(valueOf(sms.port), attrs.get(LogicaSMSChannelAdapter.PORT));
                assertEquals(sms.userName, attrs.get(LogicaSMSChannelAdapter.USERNAME));
                assertEquals(sms.password, attrs.get(LogicaSMSChannelAdapter.PASSWORD));
                assertEquals(sms.bindtype, attrs.get(LogicaSMSChannelAdapter.BINDTYPE));
                assertEquals(sms.serviceType, attrs.get(LogicaSMSChannelAdapter.SERVICE_TYPE));
                assertEquals(sms.serviceAddress, attrs.get(LogicaSMSChannelAdapter.SERVICE_ADDRESS));
                assertEquals(valueOf(sms.supportsMulti), attrs.get(LogicaSMSChannelAdapter.SUPPORTS_MULTI));
            } else if (channel instanceof ConfigValueChannelSmtp) {
                ConfigValueChannelSmtp smtp = (ConfigValueChannelSmtp) channel;
                assertEquals(smtp.hostName, attrs.get(SMTPChannelAdapter.HOST_NAME));
                assertEquals(valueOf(smtp.authorisationEnabled), attrs.get(SMTPChannelAdapter.REQUIRES_AUTH));
                assertEquals(smtp.userName, attrs.get(SMTPChannelAdapter.USER_NAME));
                assertEquals(smtp.password, attrs.get(SMTPChannelAdapter.PASSWORD));
            }
        }
    }
