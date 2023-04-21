    private void destinationChannels(Node dest, DestinationSettings destinationSettings, ServiceSettings serviceSettings) {
        String destId = destinationSettings.getId();
        String channelsList = evaluateExpression(dest, "@" + CHANNELS_ATTR).toString().trim();
        if (channelsList.length() > 0) {
            StringTokenizer st = new StringTokenizer(channelsList, LIST_DELIMITERS);
            while (st.hasMoreTokens()) {
                String ref = st.nextToken().trim();
                ChannelSettings channel = config.getChannelSettings(ref);
                if (channel != null) {
                    destinationSettings.addChannelSettings(channel);
                } else {
                    ConfigurationException ex = new ConfigurationException();
                    ex.setMessage(REF_NOT_FOUND_IN_DEST, new Object[] { CHANNEL_ELEMENT, ref, destId });
                    throw ex;
                }
            }
        } else {
            Node channelsNode = selectSingleNode(dest, CHANNELS_ELEMENT);
            if (channelsNode != null) {
                allowedChildElements(channelsNode, DESTINATION_CHANNELS_CHILDREN);
                NodeList channels = selectNodeList(channelsNode, CHANNEL_ELEMENT);
                for (int c = 0; c < channels.getLength(); c++) {
                    Node chan = channels.item(c);
                    requiredAttributesOrElements(chan, DESTINATION_CHANNEL_REQ_CHILDREN);
                    String ref = getAttributeOrChildElement(chan, REF_ATTR);
                    if (ref.length() > 0) {
                        ChannelSettings channel = config.getChannelSettings(ref);
                        if (channel != null) {
                            destinationSettings.addChannelSettings(channel);
                        } else {
                            ConfigurationException ex = new ConfigurationException();
                            ex.setMessage(REF_NOT_FOUND_IN_DEST, new Object[] { CHANNEL_ELEMENT, ref, destId });
                            throw ex;
                        }
                    } else {
                        ConfigurationException ex = new ConfigurationException();
                        ex.setMessage(INVALID_REF_IN_DEST, new Object[] { CHANNEL_ELEMENT, ref, destId });
                        throw ex;
                    }
                }
            } else {
                List defaultChannels = serviceSettings.getDefaultChannels();
                Iterator it = defaultChannels.iterator();
                while (it.hasNext()) {
                    ChannelSettings channel = (ChannelSettings) it.next();
                    destinationSettings.addChannelSettings(channel);
                }
            }
        }
        if (destinationSettings.getChannelSettings().size() <= 0) {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage(DEST_NEEDS_CHANNEL, new Object[] { destId });
            throw ex;
        }
    }
