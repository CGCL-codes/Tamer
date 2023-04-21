    private void checkMpsValue(MpsValue mpsValue, ChannelValue[] channelValues) throws ConfigurationException {
        String doc = createMpsXml(mpsValue, channelValues);
        System.err.println(doc);
        TestXmlConfigurationBuilder configBuilder = new TestXmlConfigurationBuilder(doc);
        configBuilder.addApplicationPluginRuleSet(new MpsPluginRuleSet());
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        MpsPluginConfiguration mpsPlugin = (MpsPluginConfiguration) config.getApplicationPlugin("MPS");
        if (mpsValue != null) {
            assertNotNull("mps", mpsPlugin);
            assertEquals(mpsValue.internalBaseUrl, mpsPlugin.getInternalBaseUrl());
            assertEquals(mpsValue.messageRecipientInfo, mpsPlugin.getMessageRecipientInfo());
            Iterator channels = mpsPlugin.getChannelsIterator();
            if (channelValues != null) {
                assertNotNull("channels", channels);
                assertTrue(channels.hasNext());
                for (int i = 0; i < channelValues.length; i++) {
                    MpsChannelConfiguration channel = (MpsChannelConfiguration) channels.next();
                    ChannelValue channelValue = channelValues[i];
                    checkChannel(channelValue, channel);
                }
                assertTrue(!channels.hasNext());
            } else {
                assertNotNull("channels", channels);
                assertTrue(!channels.hasNext());
            }
        } else {
            assertNull("MpsConfiguration", mpsPlugin);
        }
    }
