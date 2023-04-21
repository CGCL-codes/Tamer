    private void service(Node service) {
        requiredAttributesOrElements(service, SERVICE_REQ_CHILDREN);
        allowedAttributesOrElements(service, SERVICE_CHILDREN);
        String id = getAttributeOrChildElement(service, ID_ATTR);
        if (isValidID(id)) {
            ServiceSettings serviceSettings = config.getServiceSettings(id);
            if (serviceSettings == null) {
                serviceSettings = new ServiceSettings(id);
                NodeList properties = selectNodeList(service, PROPERTIES_ELEMENT + "/*");
                if (properties.getLength() > 0) {
                    ConfigMap map = properties(properties, getSourceFileOf(service));
                    serviceSettings.addProperties(map);
                }
                config.addServiceSettings(serviceSettings);
            } else {
                ConfigurationException e = new ConfigurationException();
                e.setMessage(DUPLICATE_SERVICE_ERROR, new Object[] { id });
                throw e;
            }
            String className = getAttributeOrChildElement(service, CLASS_ATTR);
            if (className.length() > 0) {
                serviceSettings.setClassName(className);
            } else {
                ConfigurationException ex = new ConfigurationException();
                ex.setMessage(CLASS_NOT_SPECIFIED, new Object[] { SERVICE_ELEMENT, id });
                throw ex;
            }
            Node defaultChannels = selectSingleNode(service, DEFAULT_CHANNELS_ELEMENT);
            if (defaultChannels != null) {
                allowedChildElements(defaultChannels, DEFAULT_CHANNELS_CHILDREN);
                NodeList channels = selectNodeList(defaultChannels, CHANNEL_ELEMENT);
                for (int c = 0; c < channels.getLength(); c++) {
                    Node chan = channels.item(c);
                    allowedAttributes(chan, new String[] { REF_ATTR });
                    defaultChannel(chan, serviceSettings);
                }
            } else if (config.getDefaultChannels().size() > 0) {
                for (Iterator iter = config.getDefaultChannels().iterator(); iter.hasNext(); ) {
                    String channelId = (String) iter.next();
                    ChannelSettings channel = config.getChannelSettings(channelId);
                    serviceSettings.addDefaultChannel(channel);
                }
            }
            NodeList list = selectNodeList(service, DESTINATION_ELEMENT);
            for (int i = 0; i < list.getLength(); i++) {
                Node dest = list.item(i);
                destination(dest, serviceSettings);
            }
            list = selectNodeList(service, DESTINATION_INCLUDE_ELEMENT);
            for (int i = 0; i < list.getLength(); i++) {
                Node dest = list.item(i);
                destinationInclude(dest, serviceSettings);
            }
        } else {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage(INVALID_ID, new Object[] { SERVICE_ELEMENT, id });
            throw ex;
        }
    }
